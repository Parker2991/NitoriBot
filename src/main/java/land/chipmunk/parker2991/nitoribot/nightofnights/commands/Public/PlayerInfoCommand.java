package land.chipmunk.parker2991.nitoribot.nightofnights.commands.Public;

import land.chipmunk.parker2991.nitoribot.nightofnights.Bot;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandTrustLevels;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.PlayerProfileData;
import net.kyori.adventure.text.Component;

import java.util.List;

import static land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil.convertColorString;

public class PlayerInfoCommand implements CommandInfo {
    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.PUBLIC;
    }

    @Override
    public List<String> getAliases () {
        return List.of("playerinfo", "getplayer");
    }

    @Override
    public String getDescription () {
        return "get the info of a player";
    }

    @Override
    public List<String> getUsages () {
        return List.of("<username>", "<uuid>");
    }

    @Override
    public void execute (CommandContext context) {
        Bot bot = context.bot();
        String[] args = context.args();
        CommandSource source = context.source();
        Component component = null;

        for (PlayerProfileData player : bot.players.list) {
            if (player.profile.getName().equals(args[0])) component = Component.translatable(
                "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
                Component.text("Player").color(convertColorString(bot.config.colors.commands.primary)),
                player.displayName,
                Component.text("UUID").color(convertColorString(bot.config.colors.commands.primary)),
                Component.text(player.uuid.toString()).color(convertColorString(bot.config.colors.commands.secondary)),
                Component.text("Position").color(convertColorString(bot.config.colors.commands.primary)),
                Component.text(player.position.toString()).color(convertColorString(bot.config.colors.commands.secondary)),
                Component.text("Latency").color(convertColorString(bot.config.colors.commands.primary)),
                Component.text(player.latency + "ms").color(convertColorString(bot.config.colors.commands.secondary)),
                Component.text("GameMode").color(convertColorString(bot.config.colors.commands.primary)),
                Component.text(player.gameMode.toString()).color(convertColorString(bot.config.colors.commands.secondary))
            ).color(convertColorString(bot.config.colors.commands.tertiary));
        }

        source.sendFeedback(bot, component);
    }
}
