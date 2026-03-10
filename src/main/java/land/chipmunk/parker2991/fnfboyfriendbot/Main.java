// Thanks Blackilykat and OptmisticDev and PthePro777 helping me with the client creation
package land.chipmunk.parker2991.fnfboyfriendbot;

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
// throws URISyntaxException 
public class Main {

  public Object config;

  public static final List<Bot> Bots = new ArrayList<>();

  public void loadConfig() {
    // Blackilykat helped me with the file coping and PthePro777 helped me with reading the file
    try {
      final Yaml yaml = new Yaml();
      final Path path = Path.of("config.yaml");

      if (!Files.exists(path)) {
        System.out.println("config not found making config now");

        InputStream defaultConfig = Main.class.getClassLoader().getResourceAsStream("default_config.yaml");//.getResource("default_config.yaml");//.getResource("default_config.yaml");
        Files.copy(defaultConfig, Paths.get("config.yaml"));
      }

      InputStream configFile = Files.newInputStream(path);
      Object config = yaml.load(configFile);
      this.config = config;
    } catch (final IOException error) {}
  };

  public void main(String[] args) {
    this.loadConfig();
    config = this.config;

    Bot
    Main.Bots bots = config.bots;
    //bots[] = config.bots;

    //config = this.config;
    
    //bots = config.bots;

    for (this.config.bots Bots: options) {
      System.out.println();
    };

    //Bot bot = new Bot(config);
    //System.out.println(this.config);
  };
}
