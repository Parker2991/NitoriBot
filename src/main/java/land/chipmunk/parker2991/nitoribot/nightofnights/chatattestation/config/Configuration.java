package land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public final class Configuration {
  private static final String DEFAULT_MANIFEST = "https://optmstc.dev/key-manifest-v1.json";

  public volatile List<String> keyManifestUrls = List.of(DEFAULT_MANIFEST);

  public volatile boolean toggleForSelf = true;
  public volatile boolean forceFallback = false;
  public volatile boolean disableFallback = true;
  public volatile boolean forceCompress = false;
  public volatile boolean ignoreCspyLike = true;
  public volatile int chatMsgTrunc = 2048;
  public volatile int maxCompressedPayload = this.chatMsgTrunc * 4;
}
