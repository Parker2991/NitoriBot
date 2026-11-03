package land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.config;

import io.github.wasabithumb.jtoml.configurate.TomlConfigurationLoader;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.HeaderMode;

import java.util.Objects;

import static land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util.Constants.storageDir;


public final class ConfigurationManager {
    public static final ConfigurationManager INSTANCE = new ConfigurationManager();
    public final Configuration config;
    private final CommentedConfigurationNode node;
    private final TomlConfigurationLoader loader;

    private ConfigurationManager () {
        try {
            this.loader = TomlConfigurationLoader.builder()
                .defaultOptions(opts -> opts.shouldCopyDefaults(true))
                .headerMode(HeaderMode.PRESERVE)
                .path(storageDir.resolve("chatattestation" + ".toml"))
                .build();

            this.node = this.loader.load();
            this.config = Objects.requireNonNull(this.node.get(Configuration.class));
            save();
        } catch (Exception e) {
            throw new IllegalStateException("Configuration error", e);
        }
    }

    public void save () {
        try {
            this.node.set(Configuration.class, this.config);
            this.loader.save(this.node);
        } catch (ConfigurateException e) {
            throw new IllegalStateException("Failed to save config", e);
        }
    }
}
