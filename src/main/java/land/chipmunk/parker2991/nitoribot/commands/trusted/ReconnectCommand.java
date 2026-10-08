package land.chipmunk.parker2991.nitoribot.commands.trusted;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandTrustLevels;

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
    public String getDescription () {
        return "reconnect the bot";
    }

    @Override
    public void execute (CommandContext context) {
        Bot bot = context.bot();

        bot.session.disconnect("Reconnect Command");
    }
}
