package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.*;
import land.chipmunk.parker2991.nitoribot.nightofnights.commands.Public.*;
import land.chipmunk.parker2991.nitoribot.nightofnights.commands.owner.KillCommand;
import land.chipmunk.parker2991.nitoribot.nightofnights.commands.trusted.ReconnectCommand;
import land.chipmunk.parker2991.nitoribot.nightofnights.commands.trusted.ValidateCommand;
import static land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil.convertColorString;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class CommandManagerModule {
    public final List<CommandInfo> commands = new ArrayList<>();
    private final NitoriBot bot;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManagerModule.class);

    public CommandManagerModule (NitoriBot bot) throws IOException {
        this.bot = bot;
        registerCommand(new EchoCommand());
        registerCommand(new HelpCommand());
        registerCommand(new ReconnectCommand());
        registerCommand(new RCCommand());
        registerCommand(new MCServerCommand());
        registerCommand(new TestCommand());
        registerCommand(new InfoCommand());
        registerCommand(new ListCommand());
        registerCommand(new PlayerInfoCommand());
        registerCommand(new CoreCommand());
        registerCommand(new ValidateCommand());
        registerCommand(new KillCommand());
    }

    public void registerCommand (CommandInfo command) {
        commands.add(command);
        commands.sort(Comparator.comparing((commands) -> commands.getAliases().getFirst()));
    }

    public CommandInfo getCommand (String getCommand) {
        for (CommandInfo command : commands) {
            for (String aliases : command.getAliases()) {
                if (getCommand.equals(aliases)) return command;
            }
        }
        return null;
    }

    private int getTrustLevelAsInteger (CommandTrustLevels trustLevel) {
        return switch (trustLevel) {
            case CommandTrustLevels.PUBLIC -> 0;
            case CommandTrustLevels.TRUSTED -> 1;
            case CommandTrustLevels.ADMIN -> 2;
            case CommandTrustLevels.OWNER -> 3;
            case CommandTrustLevels.CONSOLE -> 4;
            case CommandTrustLevels.DISABLED -> -69420;
        };
    }

    private Component convertTrustLevelIdToComponent (int id) {
        return switch (id) {
            case 1 -> Component.text(CommandTrustLevels.TRUSTED.toString()).color(convertColorString(bot.config.colors.help.trusted));
            case 2 -> Component.text(CommandTrustLevels.ADMIN.toString()).color(convertColorString(bot.config.colors.help.admin));
            case 3 -> Component.text(CommandTrustLevels.OWNER.toString()).color(convertColorString(bot.config.colors.help.owner));
            default -> Component.text(CommandTrustLevels.PUBLIC.toString()).color(convertColorString(bot.config.colors.help.Public));
        };
    }

    private int getDiscordRoleTrustLevelAsInteger (List<Role> role) {
        int trustLevel = 0;
        final var configRoles = bot.config.discord.roles;
        for (Role roles : role) {
            final String roleName = roles.getName();
            if (roleName.equals(configRoles.trusted)) trustLevel = 1;
            if (roleName.equals(configRoles.admin)) trustLevel = 2;
            if (roleName.equals(configRoles.owner)) trustLevel = 3;
        }

        return trustLevel;
    }

    public void execute (
        CommandSource source,
        String commandName,
        String[] args
    ) {
        try {
            CommandInfo command = getCommand(commandName.toLowerCase());

            if (command == null) throw new CommandError(Component.translatable(
                "%s%s%s %s",
                Component.translatable("command.unknown.command"),
                Component.text("\n"),
                Component.text(commandName).color(NamedTextColor.GRAY),
                Component.translatable("command.context.here")
            ).color(NamedTextColor.RED));

            CommandContext context = new CommandContext(
                bot,
                args,
                source
            );

            final int cmdTrustLevel = getTrustLevelAsInteger(command.getTrustLevel());

            if (cmdTrustLevel != 0 && cmdTrustLevel < 4) {
                if (source.source().equals(CommandMessageSource.DISCORD)) {
                    final MessageReceivedEvent event = source.discordEvent();
                    final int userTrustLevel = getDiscordRoleTrustLevelAsInteger(event.getMember().getRoles());

                    if (userTrustLevel < cmdTrustLevel) {
                        final Component playerTrustLevelComponent = convertTrustLevelIdToComponent(userTrustLevel);
                        final Component cmdTrustLevelComponent = convertTrustLevelIdToComponent(cmdTrustLevel);

                        throw new CommandError(
                            Component.translatable(
                                "Your Trust Level (%s) does not meet the required Trust Level (%s) for the command: %s",
                                playerTrustLevelComponent,
                                cmdTrustLevelComponent,
                                Component.text(commandName)
                            ).color(NamedTextColor.RED)
                        );
                    }
                } else {
                    final int playerTrustLevel = getTrustLevelAsInteger(source.sender().trustLevel);

                    if (args.length == 0)
                        throw new CommandError(Component.text("Please provide a hash").color(NamedTextColor.RED));
                    else {
                        if (args[0].equals(bot.validation.trustedHash))
                            source.sender().trustLevel = CommandTrustLevels.TRUSTED;
                        else if (args[0].equals(bot.validation.adminHash))
                            source.sender().trustLevel = CommandTrustLevels.ADMIN;
                        else if (args[0].equals(bot.validation.ownerHash))
                            source.sender().trustLevel = CommandTrustLevels.OWNER;
                    }

                    if (playerTrustLevel < cmdTrustLevel)
                        throw new CommandError(Component.text("Invalid Hash").color(NamedTextColor.RED));
                }
            }

            command.execute(context);
        } catch (CommandError error) {
            source.sendFeedback(
                bot,
                error.message()
            );
        } catch (Exception error) {
            source.sendFeedback(
                bot,
                Component.translatable("command.failed").color(NamedTextColor.DARK_RED)
            );
            LOGGER.error(
                "{}",
                CommandManagerModule.class,
                error
            );
        }
    }

    public void executeString (
        CommandSource source,
        String command
    ) {
        String[] splitArguments = command.split(" ");
        String commandName = splitArguments[0];
        final String[] args = Arrays.copyOfRange(
            splitArguments,
            1,
            splitArguments.length
        );

        this.execute(
            source,
            commandName,
            args
        );
    }
}
