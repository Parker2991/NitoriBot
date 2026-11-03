package land.chipmunk.parker2991.nitoribot.nightofnights.boot;

import land.chipmunk.parker2991.nitoribot.nightofnights.Config;
import land.chipmunk.parker2991.nitoribot.nightofnights.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoadConfig {
    private static final String XDG_CONFIG_HOME = System.getenv("XDG_CONFIG_HOME");
    private final Constructor yamlConfig = new Constructor(Config.class, new LoaderOptions());
    private final Yaml yaml = new Yaml(yamlConfig);
    private final String homeDirectory = System.getProperty("user.home");
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadConfig.class);
    public Config loadConfig () throws IOException {
        Path configDirectory;

        if (XDG_CONFIG_HOME == null) configDirectory = Path.of(homeDirectory, ".config/NitoriBot");
        else configDirectory = Path.of(XDG_CONFIG_HOME, "NitoriBot");

        final Path configPath = Path.of(String.valueOf(configDirectory), "config.yml");
        if (!Files.exists(configPath)) {
            LOGGER.warn("config not found! creating it now");

            if (!Files.exists(configDirectory)) Files.createDirectory(configDirectory);

            InputStream defaultConfig = Main.class.getClassLoader().getResourceAsStream("default_config.yml");
            if (defaultConfig != null) {
                Files.copy(defaultConfig, configPath);
            }
        }

        InputStream configFile = Files.newInputStream(configPath);

        return yaml.load(configFile);
    }
}
