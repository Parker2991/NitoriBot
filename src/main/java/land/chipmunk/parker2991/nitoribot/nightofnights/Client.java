package land.chipmunk.parker2991.nitoribot.nightofnights;

import land.chipmunk.code.kaboomstandardsorganization.messaginglib.mcprotocollib.*;
import land.chipmunk.parker2991.nitoribot.nightofnights.boot.GetProxiesList;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.nightofnights.modules.*;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectingEvent;
import org.geysermc.mcprotocollib.network.event.session.PacketErrorEvent;
import org.geysermc.mcprotocollib.network.factory.ClientNetworkSessionFactory;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundPlayerLoadedPacket;
import org.geysermc.mcprotocollib.protocol.packet.login.clientbound.ClientboundLoginFinishedPacket;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

public @NullMarked class Client extends NitoriBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(NitoriBot.class);

    public Client (
        Config.Options options,
        List<NitoriBot> bots,
        Config config
    ) throws Exception {
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
            new CommandSpyModule(this),
            this.world = new WorldModule(this),
            this.tabComplete = new TabCompleteModule(this),
            this.extrasMessaging = new ExtrasMessagingModule(this)
        );

        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private void connect () throws IOException {
        final MinecraftProtocol protocol = new MinecraftProtocol(options.username);

        if (options.useProxy) session = ClientNetworkSessionFactory.factory().setAddress(
            options.host,
            options.port
        ).setProxy(new GetProxiesList().randomProxyIp()).setProtocol(protocol).create();

        else session = ClientNetworkSessionFactory.factory().setAddress(
            options.host,
            options.port
        ).setProtocol(protocol).create();
        Injector.inject(
            session,
            this
        );
        chatAttestation.inject(session);
        session.addListener(this);
        session.connect(false);
    }



    @Override
    public void disconnecting (DisconnectingEvent event) {
        Component component = event.getReason();
        session.disconnect(component);
    }

    @Override
    public void packetSent (
        Session session,
        Packet packet
    ) {
        for (Listener listener : listenerManager.listeners) {
            listener.packetSent();
        }
    }

    @Override
    public void packetError (PacketErrorEvent error) {
        error.setSuppress(true);
    }

    @Override
    public void packetReceived (
        Session session,
        Packet packet
    ) {
        try {
            if (packet instanceof ClientboundLoginPacket) this.session.send(ServerboundPlayerLoadedPacket.INSTANCE);

            for (Listener listener : listenerManager.listeners) {
                listener.packetReceived(packet);
            }

            if (packet instanceof ClientboundLoginFinishedPacket) getProfile((ClientboundLoginFinishedPacket) packet);
            else if (packet instanceof ClientboundLoginPacket) getEntityId((ClientboundLoginPacket) packet);
        } catch (Exception _) {
        }
    }

    private void getProfile (ClientboundLoginFinishedPacket packet) {
        profile = packet.getProfile();

        loggedIn = true;
    }

    private void getEntityId (ClientboundLoginPacket packet) {
        entityId = packet.getEntityId();
    }

    @Override
    public void disconnected (DisconnectedEvent event) {
        loggedIn = false;

        Component host = Component.text(String.format(
            "[%s:%s] ",
            options.host,
            options.port
        )).color(NamedTextColor.BLUE);
        Component reason = Component.empty().append(host).append(event.getReason());
        if (services.get("reconnect") != null) services.get("reconnect").cancel(true);

        logger.reconnect(ComponentUtil.componentToAnsi(reason));

        services.put(
            "reconnect",
            executor.schedule(
                () -> {
                    try {
                        connect();
                    } catch (IOException e) {
                        LOGGER.error(
                            "{}",
                            NitoriBot.class,
                            e
                        );
                    }
                },
                options.reconnectDelay,
                TimeUnit.MILLISECONDS
            )
        );
    }
}
