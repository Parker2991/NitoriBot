package land.chipmunk.parker2991.nitoribot.command;

import java.util.List;

public interface CommandInfo {
    //String getCommandName ();

    CommandTrustLevels getTrustLevel ();

    List<String> getAliases ();

    String getDescription ();

    void execute (CommandContext context);
}