package land.chipmunk.parker2991.nitoribot.nightofnights.commands.Public;

import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandError;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandTrustLevels;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.buildstring.BotBuildInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.buildstring.RepoCommitInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.spi.SystemInfoFactory;
import oshi.spi.SystemInfoProvider;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import static land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil.convertColorString;

public class InfoCommand implements CommandInfo {
    private final Enumeration<URL> resources = InfoCommand.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
    private final Manifest manifest = new Manifest(resources.nextElement().openStream());
    private final Attributes attributes = manifest.getMainAttributes();
    private final String buildTime = attributes.getValue("Build-Time");
    private final String classPath = attributes.getValue("Main-Class");

    public InfoCommand () throws IOException {

    }

    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.PUBLIC;
    }

    @Override
    public List<String> getAliases () {
        return List.of(
            "information",
            "info"
        );
    }

    @Override
    public String getDescription () {
        return "see the bots info";
    }

    public List<String> getUsages () {
        return List.of(
            "version",
            "loaded",
            "server",
            "about"
        );
    }

    private final SystemInfoProvider si = SystemInfoFactory.create();

    private final long totalMemory = si.getHardware().getMemory().getTotal() / 1024 / 1024;

    @Override
    public void execute (CommandContext context) throws CommandError {
        var bot = context.bot();

        final BotBuildInfo info = bot.botBuildInfo;

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
                RepoCommitInfo repoCommitInfo = bot.repoCommitInfo;

                ZonedDateTime dateTime = ZonedDateTime.parse(repoCommitInfo.created);

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm:ss a");

                component = Component.translatable(
                    "%s-%s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
                    gson.deserialize(info.buildstring.botName.toString()),
                    //                        Component.text(info.buildstring.version).color(convertColorString(bot.config.colors.commands.secondary)),
                    gson.deserialize(info.buildstring.codename.toString()),
                    Component.text("Build").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(info.buildstring.build).color(convertColorString(bot.config.colors.integer)),
                    Component.text("Commit").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(repoCommitInfo.sha).color(convertColorString(bot.config.colors.commands.secondary)),
                    Component.text("Version release Date").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(dateTime.format(dateTimeFormatter)).color(convertColorString(bot.config.colors.commands.secondary)),
                    Component.text("Initial Bot Release").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(info.buildstring.initialBotRelease).color(convertColorString(bot.config.colors.commands.secondary)),
                    Component.text("Jar Compile Date").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(buildTime).color(convertColorString(bot.config.colors.commands.secondary)),
                    Component.text("Main Class Path").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(classPath).color(convertColorString(bot.config.colors.commands.secondary))
                ).color(convertColorString(bot.config.colors.commands.tertiary));
            }

            case "server" -> {
                try {
                    final String osInfo = si.getOperatingSystem().toString();
                    final HardwareAbstractionLayer hardware = si.getHardware();
                    final CentralProcessor cpuInfo = hardware.getProcessor();
                    final String cpuName = cpuInfo.getProcessorIdentifier().getName();
                    final int cpuCores = cpuInfo.getPhysicalProcessorCount();
                    final int processes = si.getOperatingSystem().getProcessCount();

                    component = Component.translatable(
                        "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
                        Component.text("Hostname").color(convertColorString(bot.config.colors.commands.primary)),
                        Component.text(InetAddress.getLocalHost().getHostName()).color(convertColorString(bot.config.colors.commands.secondary)),
                        Component.text("Username").color(convertColorString(bot.config.colors.commands.primary)),
                        Component.text(System.getProperty("user.name")).color(convertColorString(bot.config.colors.commands.secondary)),
                        Component.text("Directory").color(convertColorString(bot.config.colors.commands.primary)),
                        Component.text(System.getProperty("user.dir")).color(convertColorString(bot.config.colors.commands.secondary)),
                        Component.text("CPU Arch").color(convertColorString(bot.config.colors.commands.primary)),
                        Component.text(System.getProperty("os.arch")).color(convertColorString(bot.config.colors.commands.secondary)),
                        Component.text("OS").color(convertColorString(bot.config.colors.commands.primary)),
                        Component.text(osInfo).color(convertColorString(bot.config.colors.commands.secondary)),
                        Component.text("CPU").color(convertColorString(bot.config.colors.commands.primary)),
                        Component.text(cpuName).color(convertColorString(bot.config.colors.commands.secondary)),
                        Component.text("CPU Cores").color(convertColorString(bot.config.colors.commands.primary)),
                        Component.text(cpuCores).color(convertColorString(bot.config.colors.integer)),
                        Component.text("Processes").color(convertColorString(bot.config.colors.commands.primary)),
                        Component.text(processes).color(convertColorString(bot.config.colors.integer)),
                        Component.text("Java Version").color(convertColorString(bot.config.colors.commands.primary)),
                        Component.text(System.getProperty("java.version")).color(convertColorString(bot.config.colors.commands.secondary)),
                        Component.text("Total Memory").color(convertColorString(bot.config.colors.commands.primary)),
                        Component.text(totalMemory + " MB").color(convertColorString(bot.config.colors.commands.secondary))
                    ).color(convertColorString(bot.config.colors.commands.tertiary));
                } catch (Exception ignore) {
                }
            }

            case "about" -> component = Component.text(info.about).color(convertColorString(bot.config.colors.commands.primary));

            default -> throw new CommandError(Component.text("Unknown Argument"));
        }

        source.sendFeedback(
            bot,
            component
        );
    }
}