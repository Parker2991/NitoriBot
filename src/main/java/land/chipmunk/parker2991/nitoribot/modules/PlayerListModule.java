package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.listeners.*;

import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerInfoRemovePacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerInfoUpdatePacket;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntry;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntryAction;

import java.util.ArrayList;
import java.util.List;
import java.util.EnumSet;
import java.util.UUID;

// private final List<Listener> listeners = new ArrayList<>();
public class PlayerListModule extends Listener {
  public final List<PlayerProfileData> list = new ArrayList<>();

  @Override
  public void packetReceived (Session session, Packet packet) {
    if (packet instanceof ClientboundPlayerInfoUpdatePacket) playerInfo((ClientboundPlayerInfoUpdatePacket) packet);
    else if (packet instanceof ClientboundPlayerInfoRemovePacket) playerRemove((ClientboundPlayerInfoRemovePacket) packet);
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

  public final PlayerProfileData getPlayerUUID (UUID uuid) {
    for (PlayerProfileData player : list) {
      if (player.profile.getId().equals(uuid)) {
        return player;
      }
    };

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

    getPlayer.gameMode = player.getGameMode();
  }

  public void updateLatency (PlayerListEntry player) {
    PlayerProfileData getPlayer = getPlayerUUID(player.getProfileId());

    if (getPlayer == null) return;

    getPlayer.latency = player.getLatency();
  }

  public void updateDisplayName (PlayerListEntry player) {
    PlayerProfileData getPlayer = getPlayerUUID(player.getProfileId());

    if (getPlayer == null) return;

    getPlayer.displayName = player.getDisplayName();
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
      list.remove(getPlayer);
    }
  }

  public PlayerListModule (Bot bot) {
    bot.ListenerManager.addListener(this);
  }
}