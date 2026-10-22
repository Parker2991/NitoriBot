package land.chipmunk.parker2991.nitoribot.nightofnights.commands.Public;

import land.chipmunk.parker2991.nitoribot.nightofnights.Bot;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandTrustLevels;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.PlayerProfileData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;

import static land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil.convertColorString;

public class ListCommand implements CommandInfo {

    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.PUBLIC;
    }

    @Override
    public List<String> getAliases () {
        return List.of("list", "pl", "playerlist");
    }

    @Override
    public String getDescription () {
        return "see the list of players";
    }

    @Override
    public List<String> getUsages () {
        return null;
    }

    @Override
    public void execute (CommandContext context) {
        Bot bot = context.bot();
        CommandSource source = context.source();
        Component component = null;
        Component playerComponent = null;
        List<Component> playersComponent = new ArrayList<>();

        playerComponent = Component.translatable(
            "%s: (%s)\n",
            Component.text("Players").color(convertColorString(bot.config.colors.commands.primary)),
            Component.text(bot.players.list.size()).color(convertColorString(bot.config.colors.integer))
        ).color(convertColorString(bot.config.colors.commands.tertiary));


        for (PlayerProfileData player : bot.players.list) {
            //TranslatableComponent displayName = (TranslatableComponent) player.displayName();
            playersComponent.add(
                Component.translatable(
                    "%s › %s",
                    player.displayName.color(NamedTextColor.WHITE),
                    Component.text(player.uuid.toString()).color(convertColorString(bot.config.colors.commands.secondary))
                ).color(convertColorString(bot.config.colors.commands.tertiary))
            );
        }

        component = Component.empty().append(
            playerComponent
        ).append(
            Component.join(
                JoinConfiguration.newlines(),
                playersComponent
            )
        );

        source.sendFeedback(bot, Component.empty().append(component));
    }
}
