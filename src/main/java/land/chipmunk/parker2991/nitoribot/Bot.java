package land.chipmunk.parker2991.nitoribot;

import land.chipmunk.parker2991.nitoribot.boot.GetProxiesList;
import land.chipmunk.parker2991.nitoribot.data.buildstring.BotBuildInfo;
import land.chipmunk.parker2991.nitoribot.data.buildstring.RepoCommitInfo;
import land.chipmunk.parker2991.nitoribot.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.listeners.ListenerManager;
import land.chipmunk.parker2991.nitoribot.logger.Logger;
import land.chipmunk.parker2991.nitoribot.modules.*;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectingEvent;
import org.geysermc.mcprotocollib.network.event.session.PacketErrorEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.factory.ClientNetworkSessionFactory;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.session.ClientNetworkSession;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundPlayerLoadedPacket;
import org.geysermc.mcprotocollib.protocol.packet.login.clientbound.ClientboundLoginFinishedPacket;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

public class Bot extends SessionAdapter {
    public final ListenerManager listenerManager = new ListenerManager();
    public boolean loggedIn = false;
    public int entityId;
    public final ScheduledExecutorService executor = Main.executor;
    public final ExecutorService executorService = Main.executorService;
    public final ConcurrentMap<String, Future<?>> services = Main.services;
    public final BotBuildInfo botBuildInfo = Main.botBuildInfo;
    public final RepoCommitInfo repoCommitInfo = Main.repoCommitInfo;
    public ClientNetworkSession session;
    public GameProfile profile;
    public final @NotNull Config config;
    public final Config.Options options;
    public final List<Bot> bots;
    public final ChatModule chat;
    public final PositionModule position;
    public final CommandCoreModule core;
    public final PlayerListModule players;
    public final RegistryModule registry;
    public CommandManagerModule commandManager;
    public MCServerModule mcServer;
    public List<?> modules;

    public Bot (Config.Options options, List<Bot> bots, @NotNull Config config) throws IOException {
        this.options = options;
        this.bots = bots;
        this.config = config;

        this.modules = List.of(
            this.chat = new ChatModule(this),
            new SelfcareModule(this),
            this.position = new PositionModule(this),
            this.core = new CommandCoreModule(this),
            this.registry = new RegistryModule(this),
            this.players = new PlayerListModule(this),
            new LoggingModule(this),
            new ChatCommandHandlerModule(this),
            this.commandManager = new CommandManagerModule(this),
            new TextDisplayModule(this),
            this.mcServer = new MCServerModule(this),
            new CommandSpyModule(this)
        );

        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void connect () throws IOException {
        final MinecraftProtocol protocol = new MinecraftProtocol(options.username);

        if (options.useProxy) session = ClientNetworkSessionFactory.factory()
            .setAddress(
                options.host,
                options.port
            )
            .setProxy(new GetProxiesList().randomProxyIp())
            .setProtocol(protocol)
            .create();

        else session = ClientNetworkSessionFactory.factory()
            .setAddress(
                options.host,
                options.port
            )
            .setProtocol(protocol)
            .create();
        session.addListener(this);
        session.connect(false);
    }

    @Override
    public void disconnecting (DisconnectingEvent event) {
        Component component = event.getReason();
        session.disconnect(component);
    }

    @Override
    public void packetSent (Session session, Packet packet) {
        for (Listener listener : listenerManager.listeners) {
            listener.packetSent();
        }
    }

    @Override
    public void packetError (PacketErrorEvent error) {
        error.setSuppress(true);
    }

    @Override
    public void packetReceived (Session session, Packet packet) {
        try {
            if (packet instanceof ClientboundLoginPacket) this.session.send(
                ServerboundPlayerLoadedPacket.INSTANCE
            );

            if (packet instanceof ServerboundPlayerLoadedPacket) {
                System.out.println(packet);
            }

            for (Listener listener : listenerManager.listeners) {
                listener.packetReceived(packet);
            }

            if (packet instanceof ClientboundLoginFinishedPacket) getProfile((ClientboundLoginFinishedPacket) packet);
            else if (packet instanceof ClientboundLoginPacket) getEntityId((ClientboundLoginPacket) packet);
        } catch (Exception _) {
        }
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

        Logger.RECONNECT(this, reason);

        if (services.get("reconnect") != null) services.get("reconnect").cancel(true);

        services.put(
            "reconnect",
            executor.schedule(() -> {
                try {
                    connect();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, options.reconnectDelay, TimeUnit.MILLISECONDS)
        );
    }
}
