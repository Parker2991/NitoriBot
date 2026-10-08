package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.listeners.Listener;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundLevelChunkWithLightPacket;
import org.jetbrains.annotations.NotNull;

public class WorldModule implements Listener {
    private @NotNull Bot bot;

    public WorldModule (Bot bot) {
        this.bot = bot;

        bot.listenerManager.addListener(this);
    }

    @Override
    public void packetReceived (Packet packet) {
        switch (packet) {
            case ClientboundLevelChunkWithLightPacket p -> chunks(p);
            default -> {
            }
        }
    }

    private void chunks (ClientboundLevelChunkWithLightPacket packet) {
        //System.out.println(Arrays.toString(packet.getChunkData()));
    }
}
