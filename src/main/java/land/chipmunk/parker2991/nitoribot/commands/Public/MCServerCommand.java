package land.chipmunk.parker2991.nitoribot.commands.Public;

import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandTrustLevels;

import java.util.List;

public class MCServerCommand implements CommandInfo {
    public MCServerCommand () {

    }

    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.PUBLIC;
    }

    @Override
    public List<String> getAliases () {
        return List.of("mcserver");
    }

    @Override
    public String getDescription () {
        return "ping minecraft servers";
    }

    @Override
    public void execute (CommandContext context) {
        var bot = context.bot();
        var source = context.source();
        String args = String.join(" ", context.args());

        var ip = args.split(":");

        var info = bot.mcServer.pingServer(ip);

        System.out.println(info);

    }
}
/*
[logs] [System Chat] Ip: kaboom.pw:25565
Players: 8/0
Server Version: Paper 26.2
 Welcome to Kaboom!
   > Free OP - Anarchy - Creative
 */