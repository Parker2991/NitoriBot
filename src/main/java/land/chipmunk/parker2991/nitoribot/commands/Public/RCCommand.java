package land.chipmunk.parker2991.nitoribot.commands.Public;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.command.CommandTrustLevels;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class RCCommand extends CommandInfo {
    public RCCommand () {
        super(
            "rc",
            CommandTrustLevels.PUBLIC,
            new String[]{ "refill", "refillcore" },
            "refill the bot's core"
        );
    }

    @Override
    public void execute (CommandContext context) {
        Bot bot = context.bot();
        CommandSource source = context.source();

        bot.core.move();

        source.sendFeedback(Component.text("Refilling core").color(NamedTextColor.BLUE));
    }
}
