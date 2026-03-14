package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.logger.LoggerManager;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;

import java.util.ArrayList;
import java.util.List;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundDisguisedChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatPacket;

import net.lenni0451.lambdaevents.EventHandler;
import net.lenni0451.lambdaevents.EventManager;

public class ChatModule extends SessionAdapter {
  private Bot bot;

  private SessionAdapter session;

  public void systemChat (ClientboundSystemChatPacket packet) {

  }

  public void diguisedChat (ClientboundDisguisedChatPacket packet) {

  }

  @Override
  public void packetReceived (Session session, Packet packet) {
    // totally didnt take this from chomens java
    if (packet instanceof ClientboundSystemChatPacket) systemChat((ClientboundSystemChatPacket) packet);
    else if (packet instanceof ClientboundPlayerChatPacket) playerChat((ClientboundPlayerChatPacket) packet);
    else if (packet instanceof ClientboundDisguisedChatPacket) diguisedChat((ClientboundDisguisedChatPacket) packet);
  }

  @EventHandler(events = {EventHandler.class})
  public void playerChat (ClientboundPlayerChatPacket packet) {
    EventHandler
//    System.out.println(ComponentUtil.componentToAnsi(packet.getUnsignedContent()));
//    String stringify = ComponentUtil.componentToJSON(packet);
//    System.out.println();
  }
  //public void packetReceived (ClientboundPlayerChatPacket packet) {
    //String server = bot.options.host + ':' + bot.options.port;
    //System.out.println(packet);
 // }
  
  public ChatModule (Bot bot) {
    this.bot = bot;
    bot.session.addListener(this);
//    this.packetReceived(null, null);
    //bot.addListener(this);
    System.out.println("meow");
  }

  
}