package land.chipmunk.parker2991.nitoribot.command;

public abstract class CommandInfo {
  public String name;
  public CommandTrustLevels trustlevel;
  public String[] aliases;
  public String description;

  public CommandInfo (String name, CommandTrustLevels trustlevel, String[] aliases, String description) {
    this.name = name;
    this.trustlevel = trustlevel;
    this.aliases = aliases;
    this.description = description;
  }

  public abstract CommandContext execute (CommandContext context);
  

}