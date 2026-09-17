package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.data.PlayerPositionData;
import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.listeners.Listener;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3d;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntry;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntryAction;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerInfoRemovePacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerInfoUpdatePacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundAddEntityPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundTagQueryPacket;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class PlayerListModule extends Listener {
  private Bot bot;
  
  public final List<PlayerProfileData> list = new ArrayList<>();

  public int transactionId = 0;

  @Override
  public void packetReceived (Session session, Packet packet) {
    if (packet instanceof ClientboundPlayerInfoUpdatePacket) playerInfo((ClientboundPlayerInfoUpdatePacket) packet);
    else if (packet instanceof ClientboundPlayerInfoRemovePacket) playerRemove((ClientboundPlayerInfoRemovePacket) packet);
    else if (packet instanceof ClientboundAddEntityPacket) getPlayerPosition((ClientboundAddEntityPacket) packet);
    else if (packet instanceof ClientboundTagQueryPacket) queryPlayerData((ClientboundTagQueryPacket) packet);
  }

  public void playerInfo (ClientboundPlayerInfoUpdatePacket packet) {
    EnumSet<PlayerListEntryAction> actions = packet.getActions();

    for (PlayerListEntryAction action : actions) {
      for (PlayerListEntry entry : packet.getEntries()) {
        switch (action) {
          case PlayerListEntryAction.UPDATE_LIST_ORDER,
            PlayerListEntryAction.UPDATE_HAT,
            PlayerListEntryAction.INITIALIZE_CHAT,
            PlayerListEntryAction.ADD_PLAYER -> addPlayer(entry);
          case PlayerListEntryAction.UPDATE_LISTED -> updateListed(entry);
          case PlayerListEntryAction.UPDATE_GAME_MODE -> updateGamemode(entry);
          case PlayerListEntryAction.UPDATE_LATENCY -> updateLatency(entry);
          case PlayerListEntryAction.UPDATE_DISPLAY_NAME -> updateDisplayName(entry);
        }
      }
    }
  }

  public void queryPlayerData (ClientboundTagQueryPacket packet) {

  }

  public void getPlayerPosition (ClientboundAddEntityPacket packet) {
    if (packet.getType() == EntityType.PLAYER) {
      UUID uuid = packet.getUuid();

      int entityId = packet.getEntityId();

      Vector3d playerPosition = Vector3d.from(packet.getX(), packet.getY(), packet.getZ());

      float yaw = packet.getYaw();

      float pitch = packet.getPitch();

      PlayerProfileData player = getPlayerUUID(uuid);

      player.entityId = entityId;

      player.position = new PlayerPositionData(pitch, yaw, playerPosition);

     // bot.session.send(
       // new Server
      //)
    }
  }

  public final PlayerProfileData getPlayerByEntityID (int entityId) {
    for (PlayerProfileData player : list) {
      if (player.entityId == entityId) {
        return player;
      }
    }

    return null;
  }



  @Nullable
  public final PlayerProfileData getPlayerUUID (UUID uuid) {
    PlayerProfileData playerInfo = null;

    try {
      // lazy ass fix for the bot erroring when a player is null
      for (PlayerProfileData player : list) {
        //if (player.profile.getId() == null) {}

        UUID playerUUID = player.profile.getId();

        if (playerUUID == null) return playerInfo;
        else if (player.profile.getId().equals(uuid)) {
          playerInfo = player;
        }
      }
    } catch (Exception e) {
    }
    return playerInfo;
  }

  public final PlayerProfileData getDisplayName (Component displayName) {
    for (PlayerProfileData candidate : list) {
      if (candidate.displayName != null && candidate.displayName.equals(displayName)) {
        return candidate;
      }
    }

    return null;
  }

  public final PlayerProfileData getPlayerByUsername (String username) {
    for (PlayerProfileData candidate : list) {
      if (candidate.profile.getName().equals(username)) {
        return candidate;
      }
    }

    return null;
  }

  public void updateListed (PlayerListEntry player) {
    PlayerProfileData getPlayer = getPlayerUUID(player.getProfileId());

    if (getPlayer == null) return;

    getPlayer.listed = player.isListed();
  }

  public void updateGamemode (PlayerListEntry player) {
    PlayerProfileData getPlayer = getPlayerUUID(player.getProfileId());

    if (getPlayer == null) return;

    else getPlayer.gameMode = player.getGameMode();
  }

  public void updateLatency (PlayerListEntry player) {
    PlayerProfileData getPlayer = getPlayerUUID(player.getProfileId());

    if (getPlayer == null) return;

    else getPlayer.latency = player.getLatency();
  }

  public void updateDisplayName (PlayerListEntry player) {
    PlayerProfileData getPlayer = getPlayerUUID(player.getProfileId());

    if (getPlayer == null) return;

    else getPlayer.displayName = player.getDisplayName();
  }

  public void addPlayer (PlayerListEntry player) {
    PlayerProfileData findDuplicatePlayer = getPlayerUUID(player.getProfileId());

    if (findDuplicatePlayer != null) list.remove(findDuplicatePlayer);

    PlayerProfileData formatPlayerData = new PlayerProfileData(
      player.getProfileId(),
      player.getProfile(),
      player.isListed(),
      player.getLatency(),
      player.getGameMode(),
      player.getDisplayName()
    );

    list.add(formatPlayerData);
  }

  public void playerRemove (ClientboundPlayerInfoRemovePacket packet) {
    for (UUID uuid : packet.getProfileIds()) {
      PlayerProfileData getPlayer = getPlayerUUID(uuid);

      if (getPlayer == null) return;
      list.remove(getPlayer);
    }
  }

  public PlayerListModule (Bot bot) {
    bot.listenerManager.addListener(this);
  }
}