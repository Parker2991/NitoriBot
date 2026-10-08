package land.chipmunk.parker2991.nitoribot.commands.Public;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.command.CommandTrustLevels;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;

import java.util.ArrayList;
import java.util.List;

import static land.chipmunk.parker2991.nitoribot.util.ComponentUtil.convertColorString;

public class HelpCommand implements CommandInfo {
    public HelpCommand () {

    }

    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.PUBLIC;
    }

    @Override
    public List<String> getAliases () {
        return List.of("help", "hell", "?", "heko", "cmds", "commands");
    }

    @Override
    public String getDescription () {
        return "see the list of commands the bot has";
    }

    public Component getCommands (Bot bot) {
        List<Component> Public = new ArrayList<>();
        List<Component> Trusted = new ArrayList<>();

        for (CommandInfo commands : bot.commandManager.commands) {
            switch (commands.getTrustLevel()) {
                case CommandTrustLevels.PUBLIC -> {
                    Public.add(Component.text(commands.getAliases().getFirst() + " ").color(convertColorString(bot.config.colors.help.Public)));
                }
                case CommandTrustLevels.TRUSTED -> {
                    Trusted.add(Component.text(commands.getAliases().getFirst() + " ").color(convertColorString(bot.config.colors.help.trusted)));
                }
                default -> {
                }
            }
        }

        return Component.empty().append(Public).append(Component.text("\n")).append(Trusted);
    }

    private Component getCommand (Bot bot, String[] command) {
        Component component = null;

        for (CommandInfo commands : bot.commandManager.commands) {
            for (String aliases : commands.getAliases()) {
                if (aliases.equals(command[0])) component = Component.translatable(
                    "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
                    Component.text("Command").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(commands.getAliases().getFirst()).color(convertColorString(bot.config.colors.commands.secondary)),
                    Component.text("Aliases").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(commands.getAliases().toString()).color(convertColorString(bot.config.colors.commands.secondary)),
                    Component.text("Description").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(commands.getDescription()).color(convertColorString(bot.config.colors.commands.secondary)),
                    Component.text("Trust Level").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text(commands.getTrustLevel().toString()).color(convertColorString(bot.config.colors.commands.secondary)),
                    Component.text("Usages").color(convertColorString(bot.config.colors.commands.primary)),
                    Component.text("DUMMY STRING").color(convertColorString(bot.config.colors.commands.secondary))
                ).color(convertColorString(bot.config.colors.commands.tertiary));
            }
        }
        return component;
    }

    @Override
    public void execute (CommandContext context) {
        Bot bot = context.bot();
        String[] args = context.args();
        CommandSource source = context.source();

        if (args.length != 0) {
            source.sendFeedback(bot, getCommand(bot, args));
            return;
        }

        Component helpLayout = Component.translatable(
            "%s: (%s) (%s | %s | %s | %s) ›\n",
            Component.text("Commands").color(convertColorString(bot.config.colors.commands.primary)),
            Component.text(String.valueOf(bot.commandManager.commands.size())).color(convertColorString(bot.config.colors.integer)),
            Component.text("Public").color(convertColorString(bot.config.colors.help.Public)),
            Component.text("Trusted").color(convertColorString(bot.config.colors.help.trusted)),
            Component.text("Admin").color(convertColorString(bot.config.colors.help.admin)),
            Component.text("Owner").color(convertColorString(bot.config.colors.help.owner))
        );

        Component component = Component.empty().append(helpLayout).append(Component.join(
            JoinConfiguration.separator(Component.space()),
            getCommands(bot)
        )).color(convertColorString(bot.config.colors.commands.tertiary));

        source.sendFeedback(bot, component);
    }
}
