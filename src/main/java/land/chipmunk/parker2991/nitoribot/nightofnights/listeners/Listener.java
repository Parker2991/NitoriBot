package land.chipmunk.parker2991.nitoribot.nightofnights.listeners;

import land.chipmunk.code.kaboomstandardsorganization.messaginglib.mcprotocollib.MCProtocolLibMessenger;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.enums.SigningStatus;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.ChatType;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.chat.PlayerMessageData;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundTagQueryPacket;

import java.util.UUID;

public interface Listener {
    default void packetSent () {
    }

    default void extrasMessagingReceived (
        String channel,
        UUID uuid,
        String message
    ) {
    }

    default void chatReceived (ChatType chatType, SigningStatus signingStatus, Component message) {}

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

    default void commandSpyReceived (
        PlayerProfileData player,
        String command
    ) {
    }

    default void tabCompleteReceived (ClientboundTagQueryPacket packet) {
    }
}