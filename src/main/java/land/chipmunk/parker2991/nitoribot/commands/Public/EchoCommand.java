package land.chipmunk.parker2991.nitoribot.commands.Public;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandTrustLevels;

import java.util.List;

public class EchoCommand implements CommandInfo {
    public EchoCommand () {

    }

    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.PUBLIC;
    }

    @Override
    public List<String> getAliases () {
        return List.of("echo", "say");
    }

    @Override
    public String getDescription () {
        return "make the bot say something";
    }

    @Override
    public void execute (CommandContext context) {
        Bot bot = context.bot();
        String args = String.join(" ", context.args());
        if (args.startsWith("/")) {
            bot.chat.command(args.substring("/".length()));
        } else {
            bot.chat.message(args);
        }
    }
}
