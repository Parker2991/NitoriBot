package land.chipmunk.parker2991.nitoribot.selfcare.entity;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.listeners.Listener;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.entity.EntityEvent;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundEntityEventPacket;


public class PermissionSelfcare implements Listener {
    private final Bot bot;
    public int level;

    public PermissionSelfcare (Bot bot) {
        this.bot = bot;
        bot.listenerManager.addListener(this);
    }

    @Override
    public void packetReceived (Packet packet) {
        if (packet instanceof ClientboundEntityEventPacket) getPermissionLevel((ClientboundEntityEventPacket) packet);
    }

    private void getPermissionLevel (ClientboundEntityEventPacket packet) {
        int permEntityId = packet.getEntityId();
        final int botEntityId = bot.entityId;
        EntityEvent event = packet.getEvent();

        if (permEntityId == botEntityId) {
            switch (event) {
                case EntityEvent.PLAYER_SET_NO_PERMISSIONS -> level = 0;
                case EntityEvent.PLAYER_SET_MODERATOR -> level = 1;
                case EntityEvent.PLAYER_SET_GAMEMASTER -> level = 2;
                case EntityEvent.PLAYER_SET_ADMIN -> level = 3;
                case EntityEvent.PLAYER_SET_OWNER -> level = 4;
            }
        }
    }
}