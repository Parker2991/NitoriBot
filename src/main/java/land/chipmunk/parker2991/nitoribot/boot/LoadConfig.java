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
    private static final String XDG_CONFIG_HOME = System.getenv("XDG_CONFIG_HOME");
    private final Constructor yamlConfig = new Constructor(Config.class, new LoaderOptions());
    private final Yaml yaml = new Yaml(yamlConfig);
    private final String homeDirectory = System.getProperty("user.home");

    public Config loadConfig () throws IOException {
        Path configDirectory;

        if (XDG_CONFIG_HOME == null) configDirectory = Path.of(homeDirectory, ".config/NitoriBot");
        else configDirectory = Path.of(XDG_CONFIG_HOME, "NitoriBot");

        final Path configPath = Path.of(String.valueOf(configDirectory), "config.yml");
        if (!Files.exists(configPath)) {
            Logger.INFO(null, "config not found, creating config in " + configPath);

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
