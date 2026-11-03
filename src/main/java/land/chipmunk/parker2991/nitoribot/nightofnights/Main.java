package land.chipmunk.parker2991.nitoribot.nightofnights;

import land.chipmunk.parker2991.nitoribot.nightofnights.boot.GetBotBuildInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.boot.GetRepoInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.boot.LoadConfig;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.buildstring.BotBuildInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.buildstring.RepoCommitInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.console.CreateConsole;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.slf4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static final ExecutorService virtualThreads = Executors.newVirtualThreadPerTaskExecutor();

    public static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    public static final ConcurrentMap<String, Future<?>> services = new ConcurrentHashMap<>();
    public static final List<NitoriBot> Bots = new ArrayList<>();
    public static RepoCommitInfo repoCommitInfo = null;
    public static BotBuildInfo botBuildInfo = null;
    public static CreateConsole console;
    public Config config;
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    void main () {
        try {
            botBuildInfo = new GetBotBuildInfo().getBuildInfo();
            System.out.println("Starting NitoriBot " + ComponentUtil.componentToAnsi(GsonComponentSerializer.gson().deserialize(botBuildInfo.buildstring.codename.toString())));
            config = new LoadConfig().loadConfig();
            Config.Options[] bots = config.bots;
            console = new CreateConsole();
            repoCommitInfo = new GetRepoInfo().getRepoInfo();

            for (Config.Options options : bots) {
                final NitoriBot bot = new Client(
                    options,
                    Bots,
                    config
                );
                Bots.add(bot);
            }
        } catch (Exception e) {
            LOGGER.error("{}", Main.class, e);
        }
    }
}