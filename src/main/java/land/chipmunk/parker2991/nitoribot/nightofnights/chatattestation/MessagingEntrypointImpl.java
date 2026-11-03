package land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation;

import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.crypto.SigningManager;
import land.chipmunk.code.kaboomstandardsorganization.messaginglib.mcprotocollib.*;
/*import dev.pthepro777.messaging_lib.MCProtocolLibMessagingEntrypoint;
import dev.pthepro777.messaging_lib.MCProtocolLibMessenger;
import dev.pthepro777.messaging_lib.MCProtocolLibPayloadReceiver;*/

import io.netty.buffer.ByteBuf;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class MessagingEntrypointImpl implements MCProtocolLibMessagingEntrypoint, MCProtocolLibPayloadReceiver {
  public static final String CHANNEL_NAME = "chat-attestation/v1:stream";
  public final Map<StreamCacheKey, ByteBuf> PAYLOAD_MAP = ExpiringMap.builder()
    .maxSize(1024)
    .expiration(1, TimeUnit.MINUTES)
    .build();
  public static final Map<SigningManager.WrappedByteArray, ByteBuf> RECENT_PAYLOADS = ExpiringMap.builder()
      .maxSize(1024)
      .expiration(1, TimeUnit.MINUTES)
      .asyncExpirationListener(MessagingEntrypointImpl::onExpire)
      .build();
  public static final Map<UUID, Map<SigningManager.WrappedByteArray, ByteBuf>> ATTRIBUTED_PAYLOADS = ExpiringMap.builder()
      .maxSize(256)
      .expiration(5, TimeUnit.MINUTES)
      .expirationPolicy(ExpirationPolicy.ACCESSED)
      .build();
  public static volatile MCProtocolLibMessenger MESSENGER_INSTANCE;

  public static void onExpire(SigningManager.WrappedByteArray byteArray, ByteBuf buf) {
    buf.release();
  }


  private static Map<SigningManager.WrappedByteArray, ByteBuf> getAttributionMap(UUID sender) {
    return ATTRIBUTED_PAYLOADS.computeIfAbsent(
        sender,
        _ -> ExpiringMap.builder()
            .maxSize(16)
            .expiration(1, TimeUnit.MINUTES)
            .asyncExpirationListener(MessagingEntrypointImpl::onExpire)
            .build()
    );
  }

  @Override
  public void onRegistrationAvailable(@NotNull MCProtocolLibMessenger messenger) {
    MESSENGER_INSTANCE = messenger;

    MESSENGER_INSTANCE.receivePayloads(CHANNEL_NAME, this);
  }

  @Override
  public void onDeregister() {
    MESSENGER_INSTANCE = null;
    PAYLOAD_MAP.clear();
  }

  @Override
  public void onReceivePayload(
    @NotNull String channelName,
    @NotNull UUID sender,
    @NotNull ByteBuf payload
  ) {
    final byte[] contentHash = new byte[16];
    payload.readBytes(contentHash);

    final var wrapper = new SigningManager.WrappedByteArray(contentHash);
    RECENT_PAYLOADS.put(wrapper, payload.copy());
    getAttributionMap(sender).put(wrapper, payload.copy());
  }

  // TODO: This won't work very well with vanished players.
  public record StreamCacheKey(SigningManager.WrappedByteArray contentHash, UUID sender) {

  }
}
