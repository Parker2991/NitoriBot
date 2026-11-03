package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.world.ChunkPosData;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.world.DimensionData;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.NbtMapToJson;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundRespawnPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundLevelChunkWithLightPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundTagQueryPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level.ServerboundBlockEntityTagQueryPacket;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WorldModule implements Listener {
    private final @NotNull NitoriBot bot;
    private final Map<Integer, CompletableFuture<ClientboundTagQueryPacket>> transactions = new HashMap<>();
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
            default -> {
            }
        }
    }

    public void getBlockOutput (ClientboundTagQueryPacket packet) {
        transactions.get(packet.getTransactionId()).complete(packet);
    }

    private void respawn (ClientboundRespawnPacket packet) {
        // currentDimension = String.valueOf(packet.getCommonPlayerSpawnInfo().getWorldName());
    }

    private void addChunks (ClientboundLevelChunkWithLightPacket packet) {
        ChunkPosData pos = new ChunkPosData(
            packet.getX(),
            packet.getX()
        );

        for (DimensionData dimension : bot.registry.dimensions) {

        }
    }

    public CompletableFuture<Component> getBlock (Vector3i pos) throws ExecutionException, InterruptedException {
        bot.session.send(new ServerboundBlockEntityTagQueryPacket(
            transactionId,
            pos
        ));
        final CompletableFuture<ClientboundTagQueryPacket> future = new CompletableFuture<>();
        transactions.put(
            transactionId,
            future
        );


        return transactions.get(transactionId).thenApplyAsync((packet) -> {
            Component nbtOutput = null;
            try {
                if (packet.getTransactionId() == bot.world.transactionId) {
                    NbtMap nbt = packet.getNbt();

                    Object json = packet.getNbt().get("LastOutput");
                    if (json != null) {
                        nbtOutput = GsonComponentSerializer.gson().deserialize(
                            NbtMapToJson.INSTANCE.convert(
                                (NbtMap) packet.getNbt().get("LastOutput")
                            ).get("extra").toString()
                        );
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
            return nbtOutput;
        });

    }
}
/*
JsonObject nbtParsed = null;
                    try {

                        if (packet.getTransactionId() == bot.world.transactionId) {
                            NbtMap nbt = packet.getNbt();

                            Object json = packet.getNbt().get("LastOutput");
                            if (json != null) {
                                nbtParsed = NbtMapToJson.INSTANCE.convert((NbtMap) packet.getNbt().get("LastOutput.extra"));
                                System.out.println(nbtParsed.toString());
                              //  System.out.println(nbtParsed.toString());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }
                    return nbtParsed;
 */