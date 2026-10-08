package land.chipmunk.parker2991.nitoribot.commands.Public;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.command.CommandTrustLevels;
import net.kyori.adventure.text.Component;

import java.util.List;

import static land.chipmunk.parker2991.nitoribot.util.ComponentUtil.convertColorString;

public class RCCommand implements CommandInfo {
    public RCCommand () {
    }

    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.PUBLIC;
    }

    @Override
    public List<String> getAliases () {
        return List.of("rc", "refillcore");
    }

    @Override
    public String getDescription () {
        return "refill the bots core";
    }

    @Override
    public void execute (CommandContext context) {
        Bot bot = context.bot();
        CommandSource source = context.source();

        bot.core.move();

        source.sendFeedback(bot, Component.text("Refilling core").color(convertColorString(bot.config.colors.commands.primary)));
    }
}
