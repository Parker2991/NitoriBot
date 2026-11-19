package land.chipmunk.parker2991.nitoribot.nightofnights;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import land.chipmunk.parker2991.nitoribot.nightofnights.boot.GetBotBuildInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.boot.GetRepoInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.boot.LoadConfig;
import land.chipmunk.parker2991.nitoribot.nightofnights.console.CreateConsole;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.blocks.BlockRegistries;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.buildstring.BotBuildInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.buildstring.RepoCommitInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.discord.MessageLogger;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Main {
    private static final Gson GSON = new Gson();
    public static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public static final List<NitoriBot> Bots = new ArrayList<>();
    public static RepoCommitInfo repoCommitInfo = null;
    public static BotBuildInfo botBuildInfo = null;
    public static CreateConsole console;
    public Config config;
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static final List<BlockRegistries> blocks = new ArrayList<>();

    public static JDA discordClient;

    static {
        final List<BlockRegistries> registries = new ArrayList<>();
        final List<BlockRegistries> blockRegistries = new ArrayList<>();
        final Reader blockRegistryReader = new InputStreamReader(Main.class.getResourceAsStream("/blocks" + ".json"));
        final Reader registriesReader = new InputStreamReader(Main.class.getResourceAsStream("/registries" + ".json"));
        final Map<?, ?> map = GSON.fromJson(
            blockRegistryReader,
            Map.class
        );

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String json = GSON.toJson(entry.getValue());
            final BlockRegistries parsed = GSON.fromJson(
                json,
                BlockRegistries.class
            );
            parsed.block = entry
                .getKey()
                .toString();
            registries.add(parsed);
        }
        final JsonElement parseRegistries = JsonParser.parseReader(registriesReader);
        final JsonElement itemArray = parseRegistries
            .getAsJsonObject()
            .get("minecraft:item")
            .getAsJsonObject()
            .get("entries");
        final JsonElement blockArray = parseRegistries
            .getAsJsonObject()
            .get("minecraft:block")
            .getAsJsonObject()
            .get("entries");
        final Map<?, ?> blockEntrySet = GSON.fromJson(
            blockArray,
            Map.class
        );
        final Map<?, ?> itemEntrySet = GSON.fromJson(
            itemArray,
            Map.class
        );

        for (Map.Entry<?, ?> entry : blockEntrySet.entrySet()) {
            String key = entry
                .getKey()
                .toString();
            for (BlockRegistries block : registries) {
                final Pattern pattern = Pattern.compile("\\{protocol_id=(\\d+(?:\\.\\d+)?)\\}");
                final Matcher matcher = pattern.matcher(entry
                    .getValue()
                    .toString());
                if (matcher.find()) {
                    if (block.block.equals(key)) {
                        block.blockId = (int) Float.parseFloat(matcher.group(1));
                        blockRegistries.add(block);
                    }
                }
            }
        }

        for (Map.Entry<?, ?> entry : itemEntrySet.entrySet()) {
            final String key = entry
                .getKey()
                .toString();
            for (BlockRegistries block : blockRegistries) {
                final Pattern pattern = Pattern.compile("\\{protocol_id=(\\d+(?:\\.\\d+)?)\\}");
                final Matcher matcher = pattern.matcher(entry
                    .getValue()
                    .toString());

                if (matcher.find()) {
                    if (block.block.equals(key)) {
                        block.itemId = (int) Float.parseFloat(matcher.group(1));
                        blocks.add(block);
                    }
                }
            }
        }
    }

    void main () {
        try {
            botBuildInfo = new GetBotBuildInfo().getBuildInfo();
            System.out.println("Starting NitoriBot " + ComponentUtil.componentToAnsi(GsonComponentSerializer
                .gson()
                .deserialize(botBuildInfo.buildstring.codename.toString())));
            config = new LoadConfig().loadConfig();
            Config.Options[] bots = config.bots;
            console = new CreateConsole();
            repoCommitInfo = new GetRepoInfo().getRepoInfo();

            if (config.discord.enabled) {
                JDABuilder builder = JDABuilder.createDefault(config.discord.token);
                builder.enableIntents(
                    GatewayIntent.MESSAGE_CONTENT,
                    GatewayIntent.DIRECT_MESSAGES,
                    GatewayIntent.GUILD_MEMBERS
                );

                discordClient = builder.build();
                discordClient.addEventListener(new MessageLogger(discordClient));
                discordClient.awaitReady();
            }

            for (Config.Options options : bots) {
                final NitoriBot bot = new Client(
                    options,
                    Bots,
                    config
                );
                Bots.add(bot);
                bot.connect();
            }
        } catch (Exception e) {
            LOGGER.error(
                "{}",
                Main.class,
                e
            );
        }
    }
}