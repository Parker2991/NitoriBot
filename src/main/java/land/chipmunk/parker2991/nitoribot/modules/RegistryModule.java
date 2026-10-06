package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.listeners.Listener;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.RegistryEntry;
import org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound.ClientboundRegistryDataPacket;

import java.util.ArrayList;
import java.util.List;

public class RegistryModule extends Listener {
    public final List<String> chatTypes = new ArrayList<>();

    @Override
    public void packetReceived (Packet packet) {
        if (packet instanceof ClientboundRegistryDataPacket) packetReceived((ClientboundRegistryDataPacket) packet);
    }

    public void packetReceived (ClientboundRegistryDataPacket packet) {
        String getRegistry = packet.getRegistry().asString();
        for (RegistryEntry entry : packet.getEntries()) {
            if (getRegistry.equals("minecraft:chat_type"))
                if (entry.getData() != null) {
                    chatTypes.add(entry.getData().getCompound("chat").getString("translation_key"));
                }
        }
    }


    public RegistryModule (Bot bot) {
        bot.listenerManager.addListener(this);
    }
}