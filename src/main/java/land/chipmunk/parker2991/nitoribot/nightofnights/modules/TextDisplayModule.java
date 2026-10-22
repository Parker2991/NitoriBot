package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.Bot;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundSetEntityDataPacket;

public class TextDisplayModule implements Listener {
    private static final int TEXT_DISPLAY_ENTITY_ID = 32;

    public TextDisplayModule (Bot bot) {
        bot.listenerManager.addListener(this);
    }

    @Override
    public void packetReceived (Packet packet) {
        if (packet instanceof ClientboundSetEntityDataPacket) getTextDisplay((ClientboundSetEntityDataPacket) packet);
    }

    private void getTextDisplay (ClientboundSetEntityDataPacket packet) {
        for (EntityMetadata<?, ?> metaData : packet.getMetadata()) {
            if (metaData.getId() == TEXT_DISPLAY_ENTITY_ID) {
                Component value = (Component) metaData.getValue();
                System.out.println(ComponentUtil.componentToAnsi(value));
            }
        }
    }
}