package land.chipmunk.parker2991.nitoribot.nightofnights.commands.trusted;

import land.chipmunk.parker2991.nitoribot.nightofnights.Bot;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandTrustLevels;

import java.util.List;

public class ReconnectCommand implements CommandInfo {
    public ReconnectCommand () {

    }

    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.TRUSTED;
    }

    @Override
    public List<String> getAliases () {
        return List.of("end");
    }

    @Override
    public List<String> getUsages () {
        return null;
    }

    @Override
    public String getDescription () {
        return "reconnect the bot";
    }

    @Override
    public void execute (CommandContext context) {
        Bot bot = context.bot();

        bot.session.disconnect("Reconnect Command");
    }
}
