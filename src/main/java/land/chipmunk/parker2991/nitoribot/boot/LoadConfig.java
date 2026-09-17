package land.chipmunk.parker2991.nitoribot.boot;

import land.chipmunk.parker2991.nitoribot.Config;
import land.chipmunk.parker2991.nitoribot.Main;
import land.chipmunk.parker2991.nitoribot.logger.Logger;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoadConfig {
  public Config loadConfig () throws IOException {
    final Constructor yamlConfig = new Constructor(Config.class, new LoaderOptions());
    final Yaml yaml = new Yaml(yamlConfig);
    final Path configPath = Path.of("config.yaml");
    if (! Files.exists(configPath)) {

      Logger.INFO(null, "config not found making config now");

      InputStream defaultConfig = Main.class.getClassLoader().getResourceAsStream("default_config.yaml");
      Files.copy(defaultConfig, configPath);
    }

    InputStream configFile = Files.newInputStream(configPath);

    //config = yaml.load(configFile);
    return yaml.load(configFile);
  }

}
