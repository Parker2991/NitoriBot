package land.chipmunk.parker2991.nitoribot;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import land.chipmunk.parker2991.nitoribot.data.buildstring.BotBuildInfo;
import land.chipmunk.parker2991.nitoribot.data.buildstring.RepoCommitInfo;
import land.chipmunk.parker2991.nitoribot.modules.ConsoleModule;
import land.chipmunk.parker2991.nitoribot.logger.Logger;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class Main {
  public final Object obj = new Object();

  public static final ExecutorService executorService = Executors.newFixedThreadPool(
    Runtime.getRuntime().availableProcessors()
  );

  public static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(
    Runtime.getRuntime().availableProcessors()
  );

  public static final ConcurrentMap<String, Future<?>> services = new ConcurrentHashMap<>();

  private static final Gson GSON = new Gson();

  public static BotBuildInfo getBuildInfo () {
    Reader reader = new InputStreamReader(
      Objects.requireNonNull(Main.class
        .getResourceAsStream("/info.json"))
    );

    return GSON.fromJson(reader, BotBuildInfo.class);
  }

  public static RepoCommitInfo getRepoInfo () {
    final String repo = "https://code.chipmunk.land/api/v1/repos/Parker2991/NitoriBot/commits?sha=main";
    HttpClient client = HttpClient.newBuilder()
      .followRedirects(HttpClient.Redirect.NORMAL)
      .connectTimeout(Duration.ofSeconds(20))
      .build();

    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(repo))
      .timeout(Duration.ofSeconds(20))
      .header("accept", "application/json")
      .build();

    RepoCommitInfo info = null;
    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      String body = response.body();
      JsonElement e = JsonParser.parseString(response.body());

      info = GSON.fromJson(e.getAsJsonArray().get(0), RepoCommitInfo.class);

    } catch (Exception e) {
      e.printStackTrace(System.err);
    }

    return info;
  }

  public static RepoCommitInfo repoCommitInfo = null;// = getRepoInfo();

  public static BotBuildInfo botBuildInfo = null;// = getBuildInfo();

  public Config config;

  public static final List<Bot> Bots = new ArrayList<>();

  public Config loadConfig() throws IOException {
    final Constructor yamlConfig = new Constructor(Config.class, new LoaderOptions());
    final Yaml yaml = new Yaml(yamlConfig);
    final Path configPath = Path.of("config.yaml");
    if (!Files.exists(configPath)) {
      
      Logger.INFO(null, "config not found making config now");

      InputStream defaultConfig = Main.class.getClassLoader().getResourceAsStream("default_config.yaml");
      Files.copy(defaultConfig, configPath);
    }

    InputStream configFile = Files.newInputStream(configPath);

    config = yaml.load(configFile);
    return config;
  }

  public static ConsoleModule console;

  public void main(String[] args) {
    try {
      config = loadConfig();
      Config.Options[] bots = config.bots;

      console = new ConsoleModule(config);

      repoCommitInfo = getRepoInfo();
      botBuildInfo = getBuildInfo();

      for (Config.Options options : bots) {
        final Bot bot = new Bot(options, Bots, config);
        Bots.add(bot);
      }

    } catch (Exception e) {

    }
  };
}