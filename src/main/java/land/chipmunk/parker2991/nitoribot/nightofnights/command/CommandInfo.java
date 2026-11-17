package land.chipmunk.parker2991.nitoribot.nightofnights.command;

import net.kyori.adventure.text.Component;

import java.util.List;

public interface CommandInfo {
    CommandTrustLevels getTrustLevel ();

    List<String> getAliases ();

    String getDescription ();

    List<String> getUsages ();

    Component execute (CommandContext context) throws CommandError;
}