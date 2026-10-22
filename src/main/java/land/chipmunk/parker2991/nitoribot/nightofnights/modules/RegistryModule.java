package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import com.google.gson.Gson;
import land.chipmunk.parker2991.nitoribot.nightofnights.Bot;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.ChatTranslationsData;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.world.DimensionData;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.RegistryEntry;
import org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound.ClientboundRegistryDataPacket;

import java.util.ArrayList;
import java.util.List;

public class RegistryModule implements Listener {
    public final List<ChatTranslationsData> chatTypes = new ArrayList<>();
    public final List<DimensionData> dimensions = new ArrayList<>();
    private final Gson gson = new Gson();

    public RegistryModule (Bot bot) {
        bot.listenerManager.addListener(this);
    }

    @Override
    public void packetReceived (Packet packet) {
        if (packet instanceof ClientboundRegistryDataPacket) packetReceived((ClientboundRegistryDataPacket) packet);
    }

    public void packetReceived (ClientboundRegistryDataPacket packet) {
        String getRegistry = packet.getRegistry().asString();
        for (RegistryEntry entry : packet.getEntries()) {
            switch (getRegistry) {
                case "minecraft:chat_type" -> {
                    ChatTranslationsData parsed = gson.fromJson(
                        entry.getData().toString(),
                        ChatTranslationsData.class
                    );

                    chatTypes.add(parsed);
                }
                case "minecraft:dimension_type" -> {

                }
                default -> {}
            }
        }
    }
}