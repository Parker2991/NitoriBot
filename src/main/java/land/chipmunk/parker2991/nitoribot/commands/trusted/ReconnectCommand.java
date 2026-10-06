package land.chipmunk.parker2991.nitoribot.commands.trusted;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandTrustLevels;

public class ReconnectCommand extends CommandInfo {
    public ReconnectCommand () {
        super(
            "reconnect",
            CommandTrustLevels.TRUSTED,
            new String[]{ },
            "reconnect the bot"
        );
    }

    @Override
    public void execute (CommandContext context) {
        Bot bot = context.bot();

        bot.session.disconnect("Reconnect Command");
    }
}
