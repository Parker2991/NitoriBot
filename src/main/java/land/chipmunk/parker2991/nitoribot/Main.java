package land.chipmunk.parker2991.nitoribot;

import land.chipmunk.parker2991.nitoribot.boot.GetBotBuildInfo;
import land.chipmunk.parker2991.nitoribot.boot.GetRepoInfo;
import land.chipmunk.parker2991.nitoribot.boot.LoadConfig;
import land.chipmunk.parker2991.nitoribot.data.buildstring.BotBuildInfo;
import land.chipmunk.parker2991.nitoribot.data.buildstring.RepoCommitInfo;
import land.chipmunk.parker2991.nitoribot.modules.ConsoleModule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static final ExecutorService virtualThreads = Executors.newVirtualThreadPerTaskExecutor();

    public static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    public static final ConcurrentMap<String, Future<?>> services = new ConcurrentHashMap<>();
    public static final List<Bot> Bots = new ArrayList<>();
    public static RepoCommitInfo repoCommitInfo = null;
    public static BotBuildInfo botBuildInfo = null;
    public static ConsoleModule console;
    public Config config;

    void main () {
        try {
            config = new LoadConfig().loadConfig();
            Config.Options[] bots = config.bots;
            console = new ConsoleModule();
            repoCommitInfo = new GetRepoInfo().getRepoInfo();
            botBuildInfo = new GetBotBuildInfo().getBuildInfo();

            for (Config.Options options : bots) {
                final Bot bot = new Bot(options, Bots, config);
                Bots.add(bot);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}