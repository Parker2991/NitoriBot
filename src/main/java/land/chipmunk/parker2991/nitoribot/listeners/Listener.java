package land.chipmunk.parker2991.nitoribot.listeners;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.packet.Packet;

import net.kyori.adventure.text.Component;

public class Listener {
  public void packetSent (Session session, Packet packet) {}

  public void packetReceived (Session session, Packet packet) {}

  public void playerChatReceived (Component message) {}

  public void disguisedChatReceived (Component message) {}

  public void systemChatReceived (Component message) {}

  public void botMoved () {}
}