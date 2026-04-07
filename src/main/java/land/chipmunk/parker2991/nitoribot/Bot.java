package land.chipmunk.parker2991.nitoribot;

import land.chipmunk.parker2991.nitoribot.modules.*;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;
import land.chipmunk.parker2991.nitoribot.logger.LoggerManager;
import land.chipmunk.parker2991.nitoribot.listeners.*;
import net.kyori.adventure.text.Component;

import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.DisconnectingEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.PacketErrorEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.session.ClientNetworkSession;
import org.geysermc.mcprotocollib.network.factory.ClientNetworkSessionFactory;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.protocol.packet.login.clientbound.ClientboundLoginFinishedPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundLoginPacket;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot extends SessionAdapter {
  public final ListenerManager ListenerManager = new ListenerManager();

  public boolean loggedIn = false;

  public int entityId;

  public final ScheduledExecutorService executor = Main.executor;

  public final ExecutorService executorService = Main.executorService;

  public ClientNetworkSession session;

  public GameProfile profile;

  public Config config;

  public Config.Options options;

  public List<Bot> bots;

  public ChatModule chat;

  public ConsoleModule console;

  public SelfcareModule selfcare;

  public PositionModule position;

  public CommandCoreModule core;

  public PlayerListModule player;

  public RegistryModule registry;

  public void loadModules () {
    this.chat = new ChatModule(this);
    this.console = new ConsoleModule(this);
    this.selfcare = new SelfcareModule(this);
    this.position = new PositionModule(this);
    this.core = new CommandCoreModule(this);
    this.registry = new RegistryModule(this);
    this.player = new PlayerListModule(this);
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
  public void disconnecting (DisconnectingEvent event) {
    String reason = event + "";
    session.disconnect(reason);
  }

  @Override
  public void packetSent (Session session, Packet packet) {
    for (Listener listener : ListenerManager.listeners) {
      listener.packetSent(session, packet);
    }
  }

  @Override
  public void packetError (PacketErrorEvent error) {

  }

  @Override
  public void packetReceived (Session session, Packet packet) {
    for (Listener listener : ListenerManager.listeners) {
      listener.packetReceived(session, packet);
    }

    if (packet instanceof ClientboundLoginFinishedPacket) getProfile((ClientboundLoginFinishedPacket) packet);
    else if (packet instanceof ClientboundLoginPacket) getEntityId((ClientboundLoginPacket) packet);
  }

  
  public void getProfile (ClientboundLoginFinishedPacket packet) {
    profile = packet.getProfile();

    loggedIn = true;
  }

  public void getEntityId (ClientboundLoginPacket packet) {
    entityId = packet.getEntityId();
  }

  @Override
  public void disconnected (DisconnectedEvent event) {
    loggedIn = false;
    Component component = event.getReason();
    String reason = ComponentUtil.componentToAnsi(component);
    Component host = Component.text(options.host + ":" + options.port);
    LoggerManager.RECONNECT(host, reason);
    int reconnectDelay = options.reconnectDelay;

    executor.schedule(() -> connect(), reconnectDelay, TimeUnit.MILLISECONDS);
  }
}