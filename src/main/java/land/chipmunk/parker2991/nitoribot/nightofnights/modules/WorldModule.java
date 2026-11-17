package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.world.ChunkPosData;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.world.DimensionData;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.ChunkColumn;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.NbtMapToJson;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.ChunkSection;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundCommandSuggestionsPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundRespawnPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundForgetLevelChunkPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundLevelChunkWithLightPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundTagQueryPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level.ServerboundBlockEntityTagQueryPacket;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WorldModule implements Listener {
    private final @NotNull NitoriBot bot;
    private final Map<Integer, CompletableFuture<Component>> transactions = new HashMap<>();
    public final Map<ChunkPosData, ChunkColumn> chunks = new HashMap<>();
    public String currentDimension;
    public int transactionId = (int) Math.floor(Math.random() * 69);

    public WorldModule (NitoriBot bot) {
        this.bot = bot;

        bot.listenerManager.addListener(this);
    }

    @Override
    public void packetReceived (Packet packet) {
        switch (packet) {
            case ClientboundLevelChunkWithLightPacket p -> addChunks(p);
            case ClientboundRespawnPacket p -> respawn(p);
            case ClientboundTagQueryPacket p -> getBlockOutput(p);
            case ClientboundForgetLevelChunkPacket p -> removeChunk(p);
            default -> {
            }
        }
    }

    public void removeChunk (ClientboundForgetLevelChunkPacket packet) {
        this.chunks.remove(new ChunkPosData(packet.getX(), packet.getZ()));
    }

    private void respawn (ClientboundRespawnPacket packet) {
        currentDimension = String.valueOf(packet.getCommonPlayerSpawnInfo().getWorldName());
    }

    private void addChunks (ClientboundLevelChunkWithLightPacket packet) {
        ChunkPosData pos = new ChunkPosData(
            packet.getX(),
            packet.getX()
        );

        DimensionData dimension = bot.registry.dimensions.get(bot.world.currentDimension);
        ChunkColumn column = new ChunkColumn(pos, packet.getChunkData(), dimension.height, dimension.min_y);
        this.chunks.put(pos, column);
    }

    public void setBlock(int x, int y, int z, int id) {
        ChunkPosData chunkPos = new ChunkPosData(Math.floorDiv(x, 16), Math.floorDiv(z, 16));
        if (!this.chunks.containsKey(chunkPos)) {
            return;
        }
        this.chunks.get(chunkPos);
       // this.chunks.get(chunkPos).setBlock(x & 0xF, y, z & 0xF, id);
    }

    /*
        public final ChunkPos pos;
    public final ChunkSection[] chunks;
    private final int minY;
    public ChunkColumn(ChunkPos chunkPos, byte[] data, int worldHeight, int minY) {
        this.pos = chunkPos;
        this.minY = minY;
        int absoluteWorldHeight = Math.abs(worldHeight);
        int absoluteMinY = Math.abs(minY);
        ByteBuf in = Unpooled.wrappedBuffer(data);
        int numSections = -Math.floorDiv(-(absoluteWorldHeight + absoluteMinY), 16);
        this.chunks = new ChunkSection[numSections];
        for (int i = 0; i < numSections; ++i) {
            this.chunks[i] = MinecraftTypes.readChunkSection(in);
        }
     */
    public void getBlockOutput (ClientboundTagQueryPacket packet) {
        Component nbtOutput = null;
        try {
            if (packet.getTransactionId() == bot.world.transactionId) {
                NbtMap nbt = packet.getNbt();

                if (packet.getNbt() == null) nbtOutput = null;
                Object json = packet
                    .getNbt()
                    .get("LastOutput");
                if (json != null) {
                    nbtOutput = GsonComponentSerializer
                        .gson()
                        .deserialize(NbtMapToJson.INSTANCE
                            .convert((NbtMap) packet
                                .getNbt()
                                .get("LastOutput"))
                            .get("extra")
                            .toString());
                }
            }
        } catch (Exception e) {
            //e.printStackTrace(System.err);
        }

        transactions
            .get(packet.getTransactionId())
            .complete(nbtOutput);
    }

    public CompletableFuture<Component> getBlock (Vector3i pos)  {
        bot.session.send(new ServerboundBlockEntityTagQueryPacket(
            transactionId,
            pos
        ));
        final CompletableFuture<Component> future = new CompletableFuture<>();
        transactions.put(
            transactionId,
            future
        );

        return future;
    }
}