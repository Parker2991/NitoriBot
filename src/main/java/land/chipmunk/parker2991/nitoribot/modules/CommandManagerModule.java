package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.command.CommandError;
import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.commands.Public.*;
import land.chipmunk.parker2991.nitoribot.logger.LoggerManager;
import land.chipmunk.parker2991.nitoribot.util.ErrorToString;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandManagerModule {
  private Bot bot;

  public List<CommandInfo> commands = new ArrayList<>();

  public void registerCommand (CommandInfo command) {
    commands.add(command);
  }

  public CommandInfo getCommand (String getCommand) {
    for (CommandInfo command : commands) {
      if (getCommand.equals(command.name)) return command;
    }
    return null;
  }

  public void execute (CommandSource source, String commandName, String[] args) {
    try {
      CommandInfo command = getCommand(commandName);

      if (command == null) throw new CommandError(
        Component.translatable(
          "%s%s%s %s",
          Component.translatable("command.unknown.command"),
          Component.text("\n"),
          Component.text(commandName).color(NamedTextColor.GRAY),
          Component.translatable("command.context.here")
        ).color(NamedTextColor.RED)
      );

      CommandContext context = new CommandContext(bot, args, source, command);

      command.execute(context);

    } catch (CommandError error) {
      source.sendFeedback(error.message());
    } catch (Exception error) {
      source.sendFeedback(
        Component.translatable("command.failed").color(NamedTextColor.DARK_RED)
      );
      String Error = ErrorToString.errorToString(error);
      Component host = Component.text(bot.options.host + ":" + bot.options.port).color(NamedTextColor.BLUE);
      LoggerManager.ERROR(host, Error);
    }
  }

  public void executeString (CommandSource source, String command) {
    String[] splitArguments = command.split(" ");
    String commandName = splitArguments[0];
    final String[] args = Arrays.copyOfRange(splitArguments, 1, splitArguments.length);
    
    this.execute(source, commandName, args);
  }

  public CommandManagerModule (Bot bot) {
    this.bot = bot;

    registerCommand(new EchoCommand());
    registerCommand(new HelpCommand());
  }
}
