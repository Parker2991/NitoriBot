package land.chipmunk.parker2991.nitoribot.selfcare.entity;

import land.chipmunk.parker2991.nitoribot.Bot;

import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundEntityEventPacket;

import org.geysermc.mcprotocollib.protocol.data.game.entity.EntityEvent;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.Session;


public class PermissionSelfcare extends SessionAdapter {

  public int level;

  private Bot bot;

  @Override
  public void packetReceived (Session session, Packet packet) {
    if (packet instanceof ClientboundEntityEventPacket) getPermissionLevel((ClientboundEntityEventPacket) packet);
  }

  private void getPermissionLevel (ClientboundEntityEventPacket packet) {
    int permEntityId = packet.getEntityId();
    final int botEntityId = bot.entityId;
    EntityEvent event = packet.getEvent();

    if (permEntityId == botEntityId) {
      switch (event) {
        case EntityEvent.PLAYER_OP_PERMISSION_LEVEL_0:
          level = 0;
        break;
        case EntityEvent.PLAYER_OP_PERMISSION_LEVEL_1:
          level = 1;
        break;
        case EntityEvent.PLAYER_OP_PERMISSION_LEVEL_2:
          level = 2;
        break;
        case EntityEvent.PLAYER_OP_PERMISSION_LEVEL_3:
          level = 3;
        break;
        case EntityEvent.PLAYER_OP_PERMISSION_LEVEL_4:
          level = 4;
        break;
        default:
      }
    }
  };

  public PermissionSelfcare (Bot bot) {
    this.bot = bot;
    bot.session.addListener(this);
  }
}