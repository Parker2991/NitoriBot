package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.data.DimensionData;
import land.chipmunk.parker2991.nitoribot.listeners.Listener;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.RegistryEntry;
import org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound.ClientboundRegistryDataPacket;

import java.util.ArrayList;
import java.util.List;

public class RegistryModule implements Listener {
    public final List<String> chatTypes = new ArrayList<>();
    public final List<DimensionData> dimensions = new ArrayList<>();
    private Bot bot;


    public RegistryModule (Bot bot) {
        this.bot = bot;
        bot.listenerManager.addListener(this);
    }

    @Override
    public void packetReceived (Packet packet) {
        if (packet instanceof ClientboundRegistryDataPacket) packetReceived((ClientboundRegistryDataPacket) packet);
    }

    public void packetReceived (ClientboundRegistryDataPacket packet) {
        String getRegistry = packet.getRegistry().asString();
        for (RegistryEntry entry : packet.getEntries()) {
            if (getRegistry.equals("minecraft:chat_type")) if (entry.getData() != null) {
                chatTypes.add(entry.getData().getCompound("chat").getString("translation_key"));
            }
            if (getRegistry.equals("minecraft:worldgen/biome")) {
                System.out.println(entry);
            }
            if (getRegistry.equals("minecraft:dimension_type")) {
                if (entry.getData() != null) {
                    String dimension = null;
                    if (entry.getId().toString().equals("minecraft:overworld_caves"))
                        dimension = "minecraft:world_flatlands";
                    else dimension = String.valueOf(entry.getId());
                    this.dimensions.add(new DimensionData(
                        dimension,
                        Integer.parseInt(entry.getData().get("height").toString().replace("i", "")),
                        Integer.parseInt(entry.getData().get("min_y").toString().replace("i", ""))
                    ));
                }
            }
        }
    }
}