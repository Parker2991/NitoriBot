package land.chipmunk.parker2991.nitoribot.nightofnights.selfcare.entity;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.entity.EntityEvent;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundEntityEventPacket;


public class PermissionSelfcare implements Listener {
    private final NitoriBot bot;
    public int permLevel;

    public PermissionSelfcare (NitoriBot bot) {
        this.bot = bot;
        bot.listenerManager.addListener(this);
    }

    @Override
    public void packetReceived (Packet packet) {
        if (packet instanceof ClientboundEntityEventPacket)
            getPermissionLevel((ClientboundEntityEventPacket) packet);
    }

    private void getPermissionLevel (ClientboundEntityEventPacket packet) {
        final int botEntityId = bot.entityId;
        EntityEvent event = packet.getEvent();
        if (packet.getEntityId() != botEntityId || event.ordinal() < 24 || event.ordinal() > 28)
            return;

        permLevel = event.ordinal() - 24;
    }
}