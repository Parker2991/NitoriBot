package land.chipmunk.parker2991.nitoribot.listeners;

import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.data.chat.PlayerMessageData;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.network.packet.Packet;

public class Listener {
    public void packetSent () {}

    public void packetReceived (Packet packet) {}

    public void playerChatReceived (Component message) {}

    public void disguisedChatReceived (Component message) {}

    public void systemChatReceived (Component message) {}

    public void parsedMessage (PlayerMessageData data) {}

    public void botMoved () {}

    public void commandSpyReceived (PlayerProfileData player, String command) {}
}