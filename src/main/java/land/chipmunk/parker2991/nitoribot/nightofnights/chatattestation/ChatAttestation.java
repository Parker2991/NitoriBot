package land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation;

import io.netty.buffer.ByteBuf;
import land.chipmunk.code.kaboomstandardsorganization.messaginglib.mcprotocollib.Injector;
import land.chipmunk.parker2991.nitoribot.nightofnights.Main;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.crypto.SigningManager;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util.SigningResult;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util.Util;
import org.geysermc.mcprotocollib.network.ClientSession;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.crypto.SigningManager.createHash;

public class ChatAttestation {
    private static final UUID NIL_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static SigningResult getSigningStatus (
        String message,
        UUID senderUuid,
        String senderNick
    ) {
        final byte[] pyl;
        final var wrapped = new SigningManager.WrappedByteArray(createHash(message.getBytes(StandardCharsets.UTF_8)));
        final ByteBuf payload;
        payload = senderUuid == NIL_UUID
            ? MessagingEntrypointImpl.RECENT_PAYLOADS.remove(wrapped)
            : MessagingEntrypointImpl.ATTRIBUTED_PAYLOADS.getOrDefault(senderUuid, Collections.emptyMap()).remove(wrapped);

        if (payload == null) {
            pyl = null;
        } else {
            try {
                pyl = new byte[payload.readableBytes()];
                payload.readBytes(pyl);
            } finally {
                if (senderUuid != NIL_UUID) MessagingEntrypointImpl.RECENT_PAYLOADS.remove(wrapped);
                payload.release();
            }
        }
        return Signing.getSigningStatus(senderNick, message, pyl);

    }

    public MessagingEntrypointImpl inject (ClientSession clientNetworkSession) {
        MessagingEntrypointImpl messagingEntrypointImpl = new MessagingEntrypointImpl();
        Injector.inject(clientNetworkSession, messagingEntrypointImpl);
        return messagingEntrypointImpl;
    }
}