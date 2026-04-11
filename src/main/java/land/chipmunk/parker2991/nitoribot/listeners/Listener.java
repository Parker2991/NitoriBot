package land.chipmunk.parker2991.nitoribot.listeners;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.packet.Packet;

import land.chipmunk.parker2991.nitoribot.data.chat.PlayerMessageData;
import net.kyori.adventure.text.Component;

public class Listener {
  public void packetSent (Session session, Packet packet) {}

  public void packetReceived (Session session, Packet packet) {}

  public void playerChatReceived (Component message) {}

  public void disguisedChatReceived (Component message) {}

  public void systemChatReceived (Component message) {}

  public void parsedMessage (Component message, PlayerMessageData data) {}

  public void botMoved () {}
}