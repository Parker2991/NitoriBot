package land.chipmunk.parker2991.nitoribot.commands.Public;

import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandTrustLevels;
import land.chipmunk.parker2991.nitoribot.data.buildstring.BotBuildInfo;
import land.chipmunk.parker2991.nitoribot.data.buildstring.RepoCommitInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import static land.chipmunk.parker2991.nitoribot.util.ComponentUtil.convertColorString;

public class InfoCommand implements CommandInfo {
    private final Enumeration<URL> resources = InfoCommand.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
    private final Manifest manifest = new Manifest(resources.nextElement().openStream());
    private final Attributes attributes = manifest.getMainAttributes();
    private final String buildTime = attributes.getValue("Build-Time");

    public InfoCommand () throws IOException {

    }

    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.PUBLIC;
    }

    @Override
    public List<String> getAliases () {
        return List.of("information", "info");
    }

    @Override
    public String getDescription () {
        return "see the bots info";
    }

    @Override
    public void execute (CommandContext context) {
        var bot = context.bot();

        var source = context.source();

        var args = context.args();

        Component component = null;

        GsonComponentSerializer gson = GsonComponentSerializer.gson();

        switch (args[0].toLowerCase()) {
            case "loaded" -> component = Component.translatable(
                "%s: %s\n%s: %s\n%s: %s",
                Component.text("Modules").color(convertColorString(bot.config.colors.commands.primary)),
                Component.text(bot.modules.size()).color(convertColorString(bot.config.colors.integer)),
                Component.text("Commands").color(convertColorString(bot.config.colors.commands.primary)),
                Component.text(bot.commandManager.commands.size()).color(convertColorString(bot.config.colors.integer)),
                Component.text("Chat Parsers").color(convertColorString(bot.config.colors.commands.primary)),
                Component.text(bot.chat.chatParsers.size()).color(convertColorString(bot.config.colors.integer))
            );

            case "version" -> {
                BotBuildInfo info = bot.botBuildInfo;

                RepoCommitInfo repoCommitInfo = bot.repoCommitInfo;

                ZonedDateTime dateTime = ZonedDateTime.parse(repoCommitInfo.created);

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm:ss a");

                component = Component.translatable(
                    "%s-%s-%s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
                    gson.deserialize(info.buildstring.botName.toString()),
                    Component.text(info.buildstring.version).color(convertColorString(bot.config.colors.commands.secondary)),
                    gson.deserialize(info.buildstring.codename.toString()),
                    Component.text("Build").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(info.buildstring.build).color(convertColorString(bot.config.colors.integer)),
                    Component.text("Commit").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(repoCommitInfo.sha.substring(
                        0,
                        8
                    )).color(convertColorString(bot.config.colors.commands.secondary)),
                    Component.text("Version release Date").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(dateTime.format(dateTimeFormatter)).color(convertColorString(bot.config.colors.commands.secondary)),
                    Component.text("Initial Bot Release").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(info.buildstring.initialBotRelease).color(convertColorString(bot.config.colors.commands.secondary)),
                    Component.text("Jar Compile Date").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(buildTime).color(convertColorString(bot.config.colors.commands.secondary))
                ).color(convertColorString(bot.config.colors.commands.tertiary));
            }
        }

        source.sendFeedback(bot, component);
    }
}