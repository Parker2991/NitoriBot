package land.chipmunk.parker2991.nitoribot.nightofnights.commands.Public;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.*;
import net.kyori.adventure.text.Component;

import static land.chipmunk.parker2991.nitoribot.nightofnights.util.BetweenUtil.between;
import static land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil.convertColorString;

import java.util.List;

public class RTPCommand implements CommandInfo {
    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.PUBLIC;
    }

    @Override
    public List<String> getAliases () {
        return List.of(
            "rtp",
            "tpr",
            "randomteleport"
        );
    }

    @Override
    public String getDescription () {
        return "teleport to a random location";
    }

    @Override
    public List<String> getUsages () {
        return List.of();
    }

    @Override
    public Component execute (CommandContext context) throws CommandError {
        final NitoriBot bot = context.bot();
        final CommandSource source = context.source();
        final double posX = between(
            -1_000_000,
            1_000_000
        );
        final double posY = between(
            -1_000_000,
            1_000_000
        );
        final double posZ = between(
            -1_000_000,
            1_000_000
        );

        bot.core.run(String.format(
            "essentials:tp %s %s %s %s",
            source.sender().profile.getName(),
            posX,
            posY,
            posZ
        ));

        final Component component = Component
            .translatable(
                "teleporting %s to %s %s %s",
                Component
                    .text(source.sender().profile.getName())
                    .color(convertColorString(bot.config.colors.commands.secondary)),
                Component
                    .text(posX)
                    .color(convertColorString(bot.config.colors.integer)),
                Component
                    .text(posY)
                    .color(convertColorString(bot.config.colors.integer)),
                Component
                    .text(posZ)
                    .color(convertColorString(bot.config.colors.integer))
            )
            .color(convertColorString(bot.config.colors.commands.primary));

        return component;
    }
}
/*
const x = between(-1_000_000, 1_000_000);
    const y = 100;
    const z = between(-1_000_000, 1_000_000);
 */