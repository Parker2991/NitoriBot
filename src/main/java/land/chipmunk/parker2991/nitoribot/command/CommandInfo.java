package land.chipmunk.parker2991.nitoribot.command;

public abstract class CommandInfo {
  // probably could make this a record or interface
  public String name;
  public CommandTrustLevels trustlevel;
  public String[] aliases;
  public String description;

  public CommandInfo (
    String name,
    CommandTrustLevels trustlevel,
    String[] aliases,
    String description
  ) {
    this.name = name;
    this.trustlevel = trustlevel;
    this.aliases = aliases;
    this.description = description;
  }

  public abstract void execute (CommandContext context);


}