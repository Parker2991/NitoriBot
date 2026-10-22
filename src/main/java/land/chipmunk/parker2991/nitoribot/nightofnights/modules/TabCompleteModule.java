package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.Bot;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundCommandSuggestionsPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundCommandSuggestionPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TabCompleteModule implements Listener {
    private final Bot bot;

    private final Map<Integer, CompletableFuture<ClientboundCommandSuggestionsPacket>> transactions = new HashMap<>();

    public CompletableFuture<ClientboundCommandSuggestionsPacket> completion (String args) {
        int transationId = 10;
        bot.session.send(
            new ServerboundCommandSuggestionPacket(
                transationId,
                args
            )
        );

        final CompletableFuture<ClientboundCommandSuggestionsPacket> future = new CompletableFuture<>();
        transactions.put(transationId, future);

        return future;
    }

    @Override
    public void packetReceived (Packet packet) {
        if (packet instanceof ClientboundCommandSuggestionsPacket) commandSuggestionsReceived((ClientboundCommandSuggestionsPacket) packet);
    }

    private void commandSuggestionsReceived (ClientboundCommandSuggestionsPacket packet) {
        transactions.get(packet.getTransactionId()).complete(packet);
    }

    public TabCompleteModule (Bot bot) {
        this.bot = bot;
        bot.listenerManager.addListener(this);
    }
}
