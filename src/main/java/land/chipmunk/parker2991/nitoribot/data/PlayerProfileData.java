package land.chipmunk.parker2991.nitoribot.data;

import land.chipmunk.parker2991.nitoribot.util.UUIDUtil;

import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.auth.GameProfile;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public class PlayerProfileData {
  public UUID uuid;
  public String entityUUID;
  public final GameProfile profile;
  public boolean listed;
  public int latency;
  public GameMode gameMode;
  public Component displayName;
  public boolean showHat;
  public int listOrder;

  public PlayerProfileData (
    UUID uuid,
    GameProfile profile,
    boolean listed,
    int latency,
    GameMode gameMode,
    Component displayName
  ) {
    this.uuid = uuid;
    this.entityUUID = UUIDUtil.snbt(uuid);
    this.profile = profile;
    this.listed = listed;
    this.latency = latency;
    this.gameMode = gameMode;
    this.displayName = displayName;
  }
}