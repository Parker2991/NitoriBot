// Thanks Blackilykat and OptmisticDev and PthePro777 helping me with the client creation
package land.chipmunk.parker2991.nitoribot;

import land.chipmunk.parker2991.nitoribot.Config;
import land.chipmunk.parker2991.nitoribot.logger.LoggerManager;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public Config config;

  public final List<Bot> Bots = new ArrayList<>();

  private Config loadConfig() throws IOException {
    // Blackilykat helped me with the file coping and PthePro777 helped me with reading the file
    final Yaml yaml = new Yaml();
    final Path path = Path.of("config.yaml");

    if (!Files.exists(path)) {
      System.out.println("config not found making config now");

      InputStream defaultConfig = Main.class.getClassLoader().getResourceAsStream("default_config.yaml");//.getResource("default_config.yaml");//.getResource("default_config.yaml");
      Files.copy(defaultConfig, Paths.get("config.yaml"));
    }

    InputStream configFile = Files.newInputStream(path);
    return yaml.load(configFile);
  };

  public void main(String[] args) {
//    System.out.print("meow");
    try {
      Config config = loadConfig();
      Config.Options[] bots = config.bots;

      for (Config.Options options : bots) {
        System.out.println(options);
        final Bot bot = new Bot(options, config);
        System.out.println(bot);
        System.out.println("meow");
        Bots.add(bot);
      }
    } catch (Exception e) {
      String Error = e.getStackTrace();//.getStackTrace();
      LoggerManager.ERROR("NitoriBot Jar", Error);
    }
  };
}
