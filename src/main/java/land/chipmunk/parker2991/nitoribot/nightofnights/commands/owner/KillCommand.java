package land.chipmunk.parker2991.nitoribot.nightofnights.commands.owner;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.*;

import static land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil.convertColorString;

import net.kyori.adventure.text.Component;

import java.util.List;

public class KillCommand implements CommandInfo {
    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.OWNER;
    }

    @Override
    public List<String> getAliases () {
        return List.of(
            "kill",
            "kys",
            "stop"
        );
    }

    @Override
    public String getDescription () {
        return "kills the bot";
    }

    @Override
    public List<String> getUsages () {
        return List.of();
    }

    @Override
    public void execute (CommandContext context) throws CommandError {
        NitoriBot bot = context.bot();
        CommandSource source = context.source();

        source.sendFeedback(
            bot,
            Component.text("Killing Bot Process").color(convertColorString(bot.config.colors.commands.primary))
        );

        System.exit(1);
    }
}
