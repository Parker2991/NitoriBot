package land.chipmunk.parker2991.nitoribot.nightofnights;

import land.chipmunk.code.kaboomstandardsorganization.messaginglib.mcprotocollib.Injector;
import land.chipmunk.code.kaboomstandardsorganization.messaginglib.mcprotocollib.MCProtocolLibMessagingEntrypoint;
import land.chipmunk.code.kaboomstandardsorganization.messaginglib.mcprotocollib.MCProtocolLibMessenger;
import land.chipmunk.parker2991.nitoribot.nightofnights.boot.GetProxiesList;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.ChatAttestation;
import land.chipmunk.parker2991.nitoribot.nightofnights.console.logger.LoggerManager;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.buildstring.BotBuildInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.buildstring.RepoCommitInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.ListenerManager;
import land.chipmunk.parker2991.nitoribot.nightofnights.modules.*;
import net.dv8tion.jda.api.JDA;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.factory.ClientNetworkSessionFactory;
import org.geysermc.mcprotocollib.network.session.ClientNetworkSession;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class NitoriBot extends SessionAdapter implements MCProtocolLibMessagingEntrypoint {
    public final LoggerManager logger = new LoggerManager();
    public final ListenerManager listenerManager = new ListenerManager();
    public final ScheduledExecutorService executor = Main.executor;
    //public final ExecutorService executorService = Main.executorService;
    public final ConcurrentMap<String, Future<?>> services = Main.services;
    public final BotBuildInfo botBuildInfo = Main.botBuildInfo;
    public final RepoCommitInfo repoCommitInfo = Main.repoCommitInfo;
    public Config config;
    public Config.Options options;
    public List<NitoriBot> bots;
    public ChatModule chat;
    public PositionModule position;
    public CommandCoreModule core;
    public PlayerListModule players;
    public RegistryModule registry;
    public ValidationModule validation;
    public boolean loggedIn = false;
    public int entityId;
    public ClientNetworkSession session;
    public GameProfile profile;
    public CommandManagerModule commandManager;
    public MCServerModule mcServer;
    public WorldModule world;
    public TabCompleteModule tabComplete;
    public ExtrasMessagingModule extrasMessaging;
    public List<?> modules;
    public DiscordModule discord;
    //public final ChatAttestation chatAttestation = new ChatAttestation();

    public void connect () throws IOException {
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
        ChatAttestation.inject(session);
        session.addListener(this);
        session.connect(false);
    }

    @Override
    public void onRegistrationAvailable (MCProtocolLibMessenger messenger) {
        // stub
    }

    public NitoriBot () {
    }
}
