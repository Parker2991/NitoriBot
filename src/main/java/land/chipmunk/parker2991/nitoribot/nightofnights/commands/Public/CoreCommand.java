package land.chipmunk.parker2991.nitoribot.nightofnights.commands.Public;

import land.chipmunk.parker2991.nitoribot.nightofnights.Bot;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandTrustLevels;
import net.kyori.adventure.text.Component;

import java.util.List;

public class CoreCommand implements CommandInfo {
    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.PUBLIC;
    }

    @Override
    public List<String> getAliases () {
        return List.of("core", "cb", "corerun");
    }

    @Override
    public String getDescription () {
        return "run commands in the bots core";
    }

    @Override
    public List<String> getUsages () {
        return List.of("<command>");
    }

    @Override
    public void execute (CommandContext context) {
        Bot bot = context.bot();
        CommandSource source = context.source();
        String message = String.join(" ", context.args());

        Component output = bot.core.runTrackedWithTellraw("nitoribot.command_block.output", "@a", message);

        source.sendFeedback(bot, output);
        /*CompletableFuture.runAsync(() -> {
            source.sendFeedback(bot, bot.core.output);
            bot.core.output = null;
        }, CompletableFuture.delayedExecutor(100, TimeUnit.MILLISECONDS));*/
    }
}
