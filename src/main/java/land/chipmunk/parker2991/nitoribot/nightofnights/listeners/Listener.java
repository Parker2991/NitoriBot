package land.chipmunk.parker2991.nitoribot.nightofnights.listeners;

import land.chipmunk.parker2991.nitoribot.nightofnights.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.chat.PlayerMessageData;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundTagQueryPacket;

public interface Listener {
    default void packetSent () {
    }

    default void packetReceived (Packet packet) {
    }

    default void playerChatReceived (Component message) {
    }

    default void disguisedChatReceived (Component message) {
    }

    default void systemChatReceived (Component message) {
    }

    default void parsedMessage (PlayerMessageData data) {
    }

    default void botMoved () {
    }

    default void commandSpyReceived (PlayerProfileData player, String command) {
    }

    default void tabCompleteReceived (ClientboundTagQueryPacket packet) {}
}