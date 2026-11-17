package land.chipmunk.parker2991.nitoribot.nightofnights.commands.Public;

import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandTrustLevels;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.PingServerData;

import static land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil.convertColorString;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
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
    public List<String> getUsages () {
        return List.of("<ip>:<port>");
    }

    @Override
    public Component execute (CommandContext context) {
        var bot = context.bot();
        var source = context.source();
        String args = String.join(
            " ",
            context.args()
        );
        Component component = null;

        var ip = args.split(":");

        List<PingServerData> infoList = bot.mcServer.pingServer(ip);

        PingServerData info = infoList.getFirst();

        component = Component
            .empty()
            .append(Component.translatable(
                "%s: %s:%s\n",
                Component
                    .text("Server Ip")
                    .color(convertColorString(bot.config.colors.commands.primary)),
                Component
                    .text(ip[0])
                    .color(convertColorString(bot.config.colors.commands.secondary)),
                Component
                    .text(ip[1])
                    .color(convertColorString(bot.config.colors.commands.secondary))
            ))
            .append(Component.translatable(
                "%s: %s/%s\n",
                Component
                    .text("Players")
                    .color(convertColorString(bot.config.colors.commands.primary)),
                Component
                    .text(info.playersOnline())
                    .color(convertColorString(bot.config.colors.integer)),
                Component
                    .text(info.maxPlayers())
                    .color(convertColorString(bot.config.colors.integer))
            ))
            .append(Component.translatable(
                "%s: %s\n",
                Component
                    .text("Server Version")
                    .color(convertColorString(bot.config.colors.commands.primary)),
                Component
                    .text(info.versionName())
                    .color(convertColorString(bot.config.colors.commands.secondary))
            ))
            .append(Component.translatable(
                "%s: %s\n",
                Component
                    .text("Protocol Version")
                    .color(convertColorString(bot.config.colors.commands.primary)),
                Component
                    .text(info.protocolVersion())
                    .color(convertColorString(bot.config.colors.integer))
            ))
            .append(Component.translatable(
                "%s: \n%s",
                Component
                    .text("Description")
                    .color(convertColorString(bot.config.colors.commands.primary)),
                info.description()
            ))

            .color(convertColorString(bot.config.colors.commands.tertiary));

        return component;
    }
}