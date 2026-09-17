package land.chipmunk.parker2991.nitoribot.listeners;

import land.chipmunk.code.kaboomstandardsorganization.messaginglib.mcprotocollib.MCProtocolLibMessenger;
import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.data.chat.PlayerMessageData;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.packet.Packet;

public class Listener {
  public void packetSent (Session session, Packet packet) {}

  public void packetReceived (Session session, Packet packet) {}

  public void extrasMessagingReceived (MCProtocolLibMessenger messenger) {}

  public void chatAttestationMessageReceived () {}

  public void playerChatReceived (Component message) {}

  public void disguisedChatReceived (Component message) {}

  public void systemChatReceived (Component message) {}

  public void parsedMessage (Component message, PlayerMessageData data) {}

  public void botMoved () {}

  public void commandSpyReceived (PlayerProfileData player, String command) {}
}