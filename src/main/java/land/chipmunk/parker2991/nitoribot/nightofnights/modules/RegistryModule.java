package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import com.google.gson.*;
import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.ChatTranslationsData;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.blocks.BlockRegistries;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.world.DimensionData;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.RegistryEntry;
import org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound.ClientboundRegistryDataPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistryModule implements Listener {
    public final List<ChatTranslationsData> chatTypes = new ArrayList<>();
    public final List<DimensionData> dimensions = new ArrayList<>();
    private static final Gson GSON = new Gson();
    public final List<BlockRegistries> blocks = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryModule.class);

    public RegistryModule (NitoriBot bot) {
        bot.listenerManager.addListener(this);
        parseBlockRegistries();
    }

    @Override
    public void packetReceived (Packet packet) {
        if (packet instanceof ClientboundRegistryDataPacket) packetReceived((ClientboundRegistryDataPacket) packet);
    }

    public BlockRegistries getBlockRegistry (String block) {
        for (BlockRegistries registry : blocks) {
            if (registry.block.equals(block)) return registry;
        }

        return null;
    }

    private void parseBlockRegistries () {
        final List<BlockRegistries> registries = new ArrayList<>();
        final List<BlockRegistries> blockRegistries = new ArrayList<>();
        final Reader blockRegistryReader = new InputStreamReader(RegistryModule.class.getResourceAsStream("/blocks.json"));
        final Reader registriesReader = new InputStreamReader(RegistryModule.class.getResourceAsStream("/registries.json"));
        final Map<?, ?> map = GSON.fromJson(
            blockRegistryReader,
            Map.class
        );
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String json = GSON.toJson(entry.getValue());
            final BlockRegistries parsed = GSON.fromJson(
                json,
                BlockRegistries.class
            );
            parsed.block = entry.getKey().toString();
            registries.add(parsed);
        }
        final JsonElement parseRegistries = JsonParser.parseReader(registriesReader);
        final JsonElement itemArray = parseRegistries.getAsJsonObject().get("minecraft:item").getAsJsonObject().get("entries");
        final JsonElement blockArray = parseRegistries.getAsJsonObject().get("minecraft:block").getAsJsonObject().get("entries");
        final Map<?, ?> blockEntrySet = GSON.fromJson(
            blockArray,
            Map.class
        );
        final Map<?, ?> itemEntrySet = GSON.fromJson(
            itemArray,
            Map.class
        );


        for (Map.Entry<?, ?> entry : blockEntrySet.entrySet()) {
            String key = entry.getKey().toString();
            for (BlockRegistries block : registries) {
                final Pattern pattern = Pattern.compile("\\{protocol_id=(\\d+(?:\\.\\d+)?)\\}");
                final Matcher matcher = pattern.matcher(entry.getValue().toString());
                if (matcher.find()) {
                    if (block.block.equals(key)) {
                        block.blockId = (int) Float.parseFloat(matcher.group(1));
                        blockRegistries.add(block);
                    }
                }
            }
        }

        for (Map.Entry<?, ?> entry : itemEntrySet.entrySet()) {
            final String key = entry.getKey().toString();
            for (BlockRegistries block : blockRegistries) {
                final Pattern pattern = Pattern.compile("\\{protocol_id=(\\d+(?:\\.\\d+)?)\\}");
                final Matcher matcher = pattern.matcher(entry.getValue().toString());

                if (matcher.find()) {
                    if (block.block.equals(key)) {
                        block.itemId = (int) Float.parseFloat(matcher.group(1));
                        blocks.add(block);
                    }
                }
            }
        }
    }

    public void packetReceived (ClientboundRegistryDataPacket packet) {
        String getRegistry = packet.getRegistry().asString();
        for (RegistryEntry entry : packet.getEntries()) {
            switch (getRegistry) {
                case "minecraft:chat_type" -> {
                    ChatTranslationsData parsed = GSON.fromJson(
                        entry.getData().toString(),
                        ChatTranslationsData.class
                    );

                    chatTypes.add(parsed);
                }
                case "minecraft:dimension_type" -> {

                }
                default -> {
                }
            }
        }
    }
}