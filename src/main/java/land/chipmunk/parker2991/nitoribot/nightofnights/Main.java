package land.chipmunk.parker2991.nitoribot.nightofnights;

import land.chipmunk.parker2991.nitoribot.nightofnights.boot.GetBotBuildInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.boot.GetRepoInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.boot.LoadConfig;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.buildstring.BotBuildInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.buildstring.RepoCommitInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.console.CreateConsole;
import land.chipmunk.parker2991.nitoribot.nightofnights.discord.MessageLogger;
import land.chipmunk.parker2991.nitoribot.nightofnights.modules.DiscordModule;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.slf4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
   // public static final ExecutorService virtualThreads = Executors.newVirtualThreadPerTaskExecutor();

    public static final ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    public static final ConcurrentMap<String, Future<?>> services = new ConcurrentHashMap<>();
    public static final List<NitoriBot> Bots = new ArrayList<>();
    public static RepoCommitInfo repoCommitInfo = null;
    public static BotBuildInfo botBuildInfo = null;
    public static CreateConsole console;
    public Config config;
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static JDA discordClient;

    void main () {
        try {
            botBuildInfo = new GetBotBuildInfo().getBuildInfo();
            System.out.println("Starting NitoriBot " + ComponentUtil.componentToAnsi(GsonComponentSerializer.gson().deserialize(botBuildInfo.buildstring.codename.toString())));
            config = new LoadConfig().loadConfig();
            Config.Options[] bots = config.bots;
            console = new CreateConsole();
            repoCommitInfo = new GetRepoInfo().getRepoInfo();

            executor.setKeepAliveTime(500, TimeUnit.MILLISECONDS);
            executor.allowCoreThreadTimeOut(true);

            JDABuilder builder = JDABuilder.createDefault(config.discord.token);
            builder.enableIntents(
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_MEMBERS
            );

            discordClient = builder.build();
            discordClient.addEventListener(new MessageLogger(discordClient));
            discordClient.awaitReady();
           // discordClient.addEventListener(new MessageLogger());
            for (Config.Options options : bots) {
                final NitoriBot bot = new Client(
                    options,
                    Bots,
                    config
                );
                Bots.add(bot);
            }
            for (NitoriBot bot : Bots) {
                bot.connect();
            }
        } catch (Exception e) {
            LOGGER.error("{}", Main.class, e);
        }
    }
}