package land.chipmunk.parker2991.nitoribot.nightofnights.command;

import java.util.List;

public interface CommandInfo {
    CommandTrustLevels getTrustLevel ();

    List<String> getAliases ();

    String getDescription ();

    List<String> getUsages ();

    void execute (CommandContext context) throws CommandError;
}