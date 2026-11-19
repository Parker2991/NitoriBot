package land.chipmunk.parker2991.nitoribot.nightofnights.commands.admin;

import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandError;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandTrustLevels;
import net.kyori.adventure.text.Component;

import java.util.List;

public class NetcmdCommand implements CommandInfo {
    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.ADMIN;
    }

    @Override
    public List<String> getAliases () {
        return List.of("netcmd");
    }

    @Override
    public String getDescription () {
        return "run a command within the bot on another server";
    }

    @Override
    public List<String> getUsages () {
        return List.of();
    }

    @Override
    public Component execute (CommandContext context) throws CommandError {
        return null;
    }
}
