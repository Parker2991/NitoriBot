package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandError;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.nightofnights.commands.Public.*;
import land.chipmunk.parker2991.nitoribot.nightofnights.commands.trusted.ReconnectCommand;
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

            command. execute(context);
        } catch (CommandError error) {
            source.sendFeedback(bot, error.message());
        } catch (Exception error) {
            source.sendFeedback(bot, Component.translatable("command.failed").color(NamedTextColor.DARK_RED));
            LOGGER.error("{}", CommandManagerModule.class, error);
        }
    }

    public void executeString (CommandSource source, String command) {
        String[] splitArguments = command.split(" ");
        String commandName = splitArguments[0];
        final String[] args = Arrays.copyOfRange(splitArguments, 1, splitArguments.length);

        this.execute(source, commandName, args);
    }
}
