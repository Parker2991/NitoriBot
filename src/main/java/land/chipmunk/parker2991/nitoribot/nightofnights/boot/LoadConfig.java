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
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadConfig.class);
    public Config loadConfig () throws IOException {
        final Constructor yamlConfig = new Constructor(
            Config.class,
            new LoaderOptions()
        );
        final Yaml yaml = new Yaml(yamlConfig);
        final Path configPath = Path.of("config.yaml");
        if (!Files.exists(configPath)) {
            LOGGER.info(
                "config not found making config now"
            );

            InputStream defaultConfig = Main.class.getClassLoader().getResourceAsStream("default_config.yaml");
            Files.copy(
                defaultConfig,
                configPath
            );
        }

        InputStream configFile = Files.newInputStream(configPath);

        return yaml.load(configFile);
    }

}