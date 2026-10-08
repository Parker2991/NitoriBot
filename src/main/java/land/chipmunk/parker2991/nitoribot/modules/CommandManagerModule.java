package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandError;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.commands.Public.*;
import land.chipmunk.parker2991.nitoribot.commands.trusted.ReconnectCommand;
import land.chipmunk.parker2991.nitoribot.logger.Logger;
import land.chipmunk.parker2991.nitoribot.util.ErrorToString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManagerModule {
    public final List<CommandInfo> commands = new ArrayList<>();
    private final Bot bot;

    public CommandManagerModule (Bot bot) throws IOException {
        this.bot = bot;

        registerCommand(new EchoCommand());
        registerCommand(new HelpCommand());
        registerCommand(new ReconnectCommand());
        registerCommand(new RCCommand());
        registerCommand(new MCServerCommand());
        registerCommand(new TestCommand());
        registerCommand(new InfoCommand());
    }

    public void registerCommand (CommandInfo command) {
        commands.add(command);
    }

    public CommandInfo getCommand (String getCommand) {
        for (CommandInfo command : commands) {
            for (String aliases : command.getAliases()) {
                if (getCommand.equals(aliases)) return command;
            }
        }
        return null;
    }

    public void execute (CommandSource source, String commandName, String[] args) {
        try {
            CommandInfo command = getCommand(commandName.toLowerCase());

            if (command == null) throw new CommandError(Component.translatable(
                "%s%s%s %s",
                Component.translatable("command.unknown.command"),
                Component.text("\n"),
                Component.text(commandName).color(NamedTextColor.GRAY),
                Component.translatable("command.context.here")
            ).color(NamedTextColor.RED));

            CommandContext context = new CommandContext(bot, args, source);

            command.execute(context);
        } catch (CommandError error) {
            source.sendFeedback(bot, error.message());
        } catch (Exception error) {
            source.sendFeedback(bot, Component.translatable("command.failed").color(NamedTextColor.DARK_RED));
            String Error = ErrorToString.errorToString(error);
            Logger.ERROR(bot, Error);
        }
    }

    public void executeString (CommandSource source, String command) {
        String[] splitArguments = command.split(" ");
        String commandName = splitArguments[0];
        final String[] args = Arrays.copyOfRange(splitArguments, 1, splitArguments.length);

        this.execute(source, commandName, args);
    }
}
