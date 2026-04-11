package land.chipmunk.parker2991.nitoribot.command;

import net.kyori.adventure.text.Component;

public class CommandError extends Exception {
  public String name = "CommandError";
  public Component message;

  public CommandError (Component message) {
    this.message = message;
  }

  public Component message () {
    return message;
  }
}