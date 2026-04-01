package land.chipmunk.parker2991.nitoribot;

import land.chipmunk.parker2991.nitoribot.logger.LoggerManager;
import land.chipmunk.parker2991.nitoribot.util.ErrorToString;

import net.kyori.adventure.text.Component;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
  public final Object obj = new Object();

  public static final ExecutorService executorService = Executors.newFixedThreadPool(
    Runtime.getRuntime().availableProcessors()
  );

  public static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(
    Runtime.getRuntime().availableProcessors()
  );

  public final Component host = Component.text("Nitori Jar");

  public static Config config;

  public final List<Bot> Bots = new ArrayList<>();

  public Config loadConfig() throws IOException {
    final Constructor yamlConfig = new Constructor(Config.class, new LoaderOptions());
    final Yaml yaml = new Yaml(yamlConfig);
    final Path configPath = Path.of("config.yaml");
    if (!Files.exists(configPath)) {
      
      LoggerManager.INFO(host, "config not found making config now");

      InputStream defaultConfig = Main.class.getClassLoader().getResourceAsStream("default_config.yaml");
      Files.copy(defaultConfig, Paths.get("config.yaml"));
    }

    InputStream configFile = Files.newInputStream(configPath);

    config = yaml.load(configFile);
    return config;
  }

  public void main(String[] args) {
    try {
      config = loadConfig();
      Config.Options[] bots = config.bots;

      for (Config.Options options : bots) {
        final Bot bot = new Bot(options, Bots, config);
        Bots.add(bot);
      }
      synchronized(obj) {
        obj.wait();
      };
    } catch (Exception e) {
      String Error = ErrorToString.errorToString(e);
      LoggerManager.ERROR(host, Error);
    }
  };
}