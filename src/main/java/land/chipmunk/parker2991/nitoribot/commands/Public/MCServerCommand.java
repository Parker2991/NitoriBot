package land.chipmunk.parker2991.nitoribot.commands.Public;

import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandTrustLevels;

public class MCServerCommand extends CommandInfo {
    public MCServerCommand () {
        super(
            "mcserver",
            CommandTrustLevels.PUBLIC,
            new String[]{ "minecraftserver" },
            "ping a minecraft server"
        );
    }

    @Override
    public void execute (CommandContext context) {
        var bot = context.bot();
        var source = context.source();
        String args = String.join(" ", context.args());

        var ip = args.split(":");

        var info = bot.mcServer.pingServer(ip);

        System.out.println(info);

    /*source.sendFeedback(
      Component.translatable(
        "%s: (%s / %s)",
        Component.text("Players").color(NamedTextColor.BLUE),
        Component.text(info.playersOnline()).color(NamedTextColor.GOLD),
        Component.text(info.maxPlayers()).color(NamedTextColor.GOLD)
      )
    );*/

    /*source.sendFeedback(
      Component.translatable(
        "Server Version %s %s %s",
        Component.text(":").color(NamedTextColor.GRAY),
        Component.text(info.versionName()),
        Component.text(info.protocolVersion())
      )
    );*/

        //source.sendFeedback(info.description());
    }
}
/*
[logs] [System Chat] Ip: kaboom.pw:25565
Players: 8/0
Server Version: Paper 26.2
 Welcome to Kaboom!
   > Free OP - Anarchy - Creative
 */