package land.chipmunk.parker2991.nitoribot.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jline.shell.Command;

public class CommandArguments {
  private CommandSource source;
  private CommandInfo command;
  public String[] arguments;

  public CommandArguments (
    CommandSource source,
    CommandInfo command,
    String[] arguments
  ) {
    this.source = source;
    this.command = command;
    this.arguments = arguments;
  }

 /* public static void getArguments (
    CommandSource source,
    CommandInfo command,
    String[] args
  ) throws CommandError {

    System.out.println(args.length);
    if (
      args.length > command.maxArguments
    ) {
      throw new CommandError(
        Component.translatable(
          "Too many arguments, expected %s max not %s",
          Component.text(command.maxArguments),
          Component.text(args.length)
        ).color(NamedTextColor.RED)
      );
    }

    if (
      args.length < command.minArguments
    ) {
      throw new CommandError(
        Component.translatable(
          "Expected more than %s arguments recieved %s",
          Component.text(command.maxArguments),
          Component.text(args.length)
        ).color(NamedTextColor.RED)
      );
    }
  }*/



  //public void checkArgumentType ()
}
