package land.chipmunk.parker2991.nitoribot.selfcare.entity;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.listeners.*;

import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundGameEventPacket;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.GameEvent;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.GameEventValue;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.Session;

public class GamemodeSelfcare extends Listener {
  public int gamemode = 1;

  @Override
  public void packetReceived (Session session, Packet packet) {
    if (packet instanceof ClientboundGameEventPacket) checkGamemode((ClientboundGameEventPacket) packet);
  }

  private void checkGamemode (ClientboundGameEventPacket packet) {
    GameEvent notification = packet.getNotification();
    GameEventValue value = packet.getValue();

    if (notification == GameEvent.CHANGE_GAME_MODE) {
      switch (value) {
        case GameMode.SURVIVAL:
          gamemode = 0;
        break;
        case GameMode.CREATIVE:
          gamemode = 1;
        break;
        case GameMode.ADVENTURE:
          gamemode = 2;
        break;
        case GameMode.SPECTATOR:
          gamemode = 3;
        break;
        default:
      }
    }
  }

  public GamemodeSelfcare (Bot bot) {
    bot.ListenerManager.addListener(this);
  }
}
