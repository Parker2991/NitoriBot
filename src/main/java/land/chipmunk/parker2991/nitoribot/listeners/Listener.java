package land.chipmunk.parker2991.nitoribot.listeners;

import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.data.chat.PlayerMessageData;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.network.packet.Packet;

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
}