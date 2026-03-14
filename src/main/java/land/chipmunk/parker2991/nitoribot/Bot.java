package land.chipmunk.parker2991.nitoribot;

import land.chipmunk.parker2991.nitoribot.modules.*;

import java.util.List;

import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.DisconnectingEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.session.ClientNetworkSession;
import org.geysermc.mcprotocollib.network.factory.ClientNetworkSessionFactory;
import org.geysermc.mcprotocollib.network.packet.Packet;


public class Bot extends SessionAdapter {
//  public final List<Listener> listeners = new ArrayList<>();

  public ClientNetworkSession session;

  public Config config;

  public Config.Options options;

  public List<Bot> bots;

  public ChatModule chat;

  public void loadModules () {
    this.chat = new ChatModule(this);
  }

  public Bot (Config.Options options, List<Bot> bots, Config config) {
    this.options = options;
    this.bots = bots;
    this.config = config;
    connect();

  };

  public void connect () {
    final MinecraftProtocol protocol = new MinecraftProtocol(options.username);

    session = ClientNetworkSessionFactory.factory()
      .setAddress(
         options.host,
         options.port
      )
      .setProtocol(protocol)
      .create();
      session.addListener(this);
    loadModules();
    session.connect(false);
    
  }

  @Override
  public void disconnecting (DisconnectingEvent event) {}

  @Override
  public void packetReceived(Session session, Packet packet) {
    session.send(packet);
  }

}
