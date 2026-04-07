package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.listeners.*;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound.ClientboundRegistryDataPacket;
import org.geysermc.mcprotocollib.protocol.data.game.RegistryEntry;

import java.util.List;
import java.util.ArrayList;

public class RegistryModule extends Listener {
  public final List<String> chatTypes = new ArrayList<>();

  @Override
  public void packetReceived (Session session, Packet packet) {
    if (packet instanceof ClientboundRegistryDataPacket) packetReceived((ClientboundRegistryDataPacket) packet); 
  }

  public void packetReceived (ClientboundRegistryDataPacket packet) {
    String getRegistry = packet.getRegistry().asString();
    for (RegistryEntry entry : packet.getEntries()) {
      if (getRegistry.equals("minecraft:chat_type")) 
        chatTypes.add(entry.getData().getCompound("chat").getString("translation_key"));
    }
  }

  public RegistryModule (Bot bot) {
    bot.ListenerManager.addListener(this);
  }
}