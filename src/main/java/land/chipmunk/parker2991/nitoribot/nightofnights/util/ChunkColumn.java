package land.chipmunk.parker2991.nitoribot.nightofnights.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.world.ChunkPosData;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.chunk.ChunkSection;

import java.util.Arrays;

public final class ChunkColumn {
    private static final int BLOCK_STATE_REGISTRY_SIZE = 27000;
    private static final int BIOME_REGISTRY_SIZE = 64;

    private final ChunkPosData pos;
    private final ChunkSection[] chunks;
    private final int minY;

    public ChunkColumn(ChunkPosData chunkPos, byte[] data, int worldHeight, int minY) {
        this.pos = chunkPos;
        this.minY = minY;
        ByteBuf in = Unpooled.wrappedBuffer(data);
        int numSections = worldHeight / 16;
        this.chunks = new ChunkSection[numSections];
        for (int i = 0; i < numSections && in.isReadable(); ++i) {
            this.chunks()[i] = MinecraftTypes.readChunkSection(in, BLOCK_STATE_REGISTRY_SIZE, BIOME_REGISTRY_SIZE);
        }
    }

    public int getBlock(int x, int y, int z) {
        if (this.chunks() == null) {
            return 0;
        }
        int yIndex = y - this.minY >> 4;
        if (yIndex < 0 || yIndex >= this.chunks().length) {
            return 0;
        }
        ChunkSection section = this.chunks()[yIndex];
        if (section == null) {
            return 0;
        }
        return section.getBlock(x, y & 0xF, z);
    }

    public ChunkPosData pos() {
        return pos;
    }

    public ChunkSection[] chunks() {
        return Arrays.copyOf(chunks, chunks.length);
    }
}