package land.chipmunk.parker2991.nitoribot.command;

public abstract class CommandInfo {
    // probably could make this a record or interface
    public final String name;
    public final CommandTrustLevels trustlevel;
    public final String[] aliases;
    public final String description;

    public CommandInfo (String name, CommandTrustLevels trustlevel, String[] aliases, String description) {
        this.name = name;
        this.trustlevel = trustlevel;
        this.aliases = aliases;
        this.description = description;
    }

    public abstract void execute (CommandContext context);


}