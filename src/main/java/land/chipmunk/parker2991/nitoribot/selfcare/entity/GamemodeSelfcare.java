package land.chipmunk.parker2991.nitoribot.selfcare.entity;

import land.chipmunk.parker2991.nitoribot.Bot;

import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundGameEventPacket;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.GameEvent;
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.GameEventValue;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.Session;

public class GamemodeSelfcare extends SessionAdapter {
  private Bot bot;

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
    this.bot = bot;
    bot.session.addListener(this);
  }
}
