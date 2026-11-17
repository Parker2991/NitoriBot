package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import com.google.gson.*;
import land.chipmunk.parker2991.nitoribot.nightofnights.Main;
import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.chat.ChatTranslationsData;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.blocks.BlockRegistries;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.world.DimensionData;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.NbtMapToJson;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.RegistryEntry;
import org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound.ClientboundRegistryDataPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistryModule implements Listener {
    public final List<ChatTranslationsData> chatTypes = new ArrayList<>();
    public final Map<String, DimensionData> dimensions = new HashMap<>();
    private static final Gson GSON = new Gson();
    public final List<BlockRegistries> blocks = Main.blocks;
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryModule.class);

    public RegistryModule (NitoriBot bot) {
        bot.listenerManager.addListener(this);
       // parseBlockRegistries();
    }

    @Override
    public void packetReceived (Packet packet) {
        if (packet instanceof ClientboundRegistryDataPacket)
            packetReceived((ClientboundRegistryDataPacket) packet);
    }

    public BlockRegistries getBlockByStateId (int id) {
        for (BlockRegistries entry : blocks) {
            for (BlockRegistries.STATES states : entry.states) {
                if (id == states.id) return entry;
            }
        }

        return null;
    }

    public void packetReceived (ClientboundRegistryDataPacket packet) {
        String getRegistry = packet
            .getRegistry()
            .asString();
        for (RegistryEntry entry : packet.getEntries()) {
            switch (getRegistry) {
                case "minecraft:chat_type" -> {
                    ChatTranslationsData parsed = GSON.fromJson(
                        entry
                            .getData()
                            .toString(),
                        ChatTranslationsData.class
                    );

                    chatTypes.add(parsed);
                }
                case "minecraft:dimension_type" -> {
                    final String id = entry.getId().toString();
                    final Object data = entry.getData();
                    String worldName;
                    try {
                        var nbtMap = NbtMapToJson.INSTANCE.convert((NbtMap) data);

                        DimensionData parsed = GSON.fromJson(
                            nbtMap.toString(),
                            DimensionData.class
                        );

                        if (id.equals("minecraft:overworld_caves")) worldName = "minecraft:world_flatlands";
                        else worldName = id;

                        dimensions.put(worldName, parsed);
                    } catch (Exception e) {
                        LOGGER.error("{}", RegistryModule.class, e);
                    }
                }
                default -> {
                }
            }
        }
    }

    public BlockRegistries getBlockRegistry (String block) {
        for (BlockRegistries registry : blocks) {
            if (registry.block.equals(block))
                return registry;
        }

        return null;
    }
}