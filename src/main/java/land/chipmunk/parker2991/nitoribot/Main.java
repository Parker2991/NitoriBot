package land.chipmunk.parker2991.nitoribot;

import land.chipmunk.parker2991.nitoribot.boot.*;
import land.chipmunk.parker2991.nitoribot.data.buildstring.BotBuildInfo;
import land.chipmunk.parker2991.nitoribot.data.buildstring.RepoCommitInfo;
import land.chipmunk.parker2991.nitoribot.modules.ConsoleModule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
  public static final ExecutorService executorService = Executors.newFixedThreadPool(
    Runtime.getRuntime().availableProcessors()
  );

  public static final ExecutorService virtualThreads = Executors.newVirtualThreadPerTaskExecutor();

  public static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(
    Runtime.getRuntime().availableProcessors()
  );

  public static final ConcurrentMap<String, Future<?>> services = new ConcurrentHashMap<>();

  public static RepoCommitInfo repoCommitInfo = null;

  public static BotBuildInfo botBuildInfo = null;

  public Config config;

  public static final List<Bot> Bots = new ArrayList<>();

  public static ConsoleModule console;

  public void main(String[] args) {
    try {
      config = new LoadConfig().loadConfig();
      Config.Options[] bots = config.bots;

      console = new ConsoleModule(config);

      repoCommitInfo = new GetRepoInfo().getRepoInfo();
      botBuildInfo = new GetBotBuildInfo().getBuildInfo();

      for (Config.Options options : bots) {
        final Bot bot = new Bot(options, Bots, config);
        Bots.add(bot);
      }

    } catch (Exception e) {

    }
  };
}