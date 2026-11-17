package land.chipmunk.parker2991.nitoribot.nightofnights.commands.Public;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.*;

import static land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil.convertColorString;

import land.chipmunk.parker2991.nitoribot.nightofnights.data.PlayerProfileData;
import net.kyori.adventure.text.Component;

import java.util.List;

public class NetmsgCommand implements CommandInfo {

    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.PUBLIC;
    }

    @Override
    public List<String> getAliases () {
        return List.of(
            "netmsg",
            "networkmessage"
        );
    }

    @Override
    public String getDescription () {
        return "send a message to the servers the bot is connected to";
    }

    @Override
    public List<String> getUsages () {
        return List.of("<message>");
    }

    @Override
    public Component execute (CommandContext context) throws CommandError {
        final NitoriBot bot = context.bot();
        final String message = String
            .join(
                " ",
                context.args()
            )
            .replaceAll(
                ".*nigger.*",
                "I love faggots, I love the gays"
            )
            .replaceAll(
                ".*skid.*",
                "im a no life retarded piece of shit!"
            )
            .replaceAll(
                ".*meow.*",
                ":3"
            );
        final CommandSource source = context.source();

        for (NitoriBot eachBot : bot.bots) {
            Component component = null;
            if (eachBot.options.isPrivate)
                component = Component
                    .translatable(
                        "[%s] %s › %s",
                        Component
                            .text(eachBot.options.serverName)
                            .color(convertColorString(bot.config.colors.commands.primary)),
                        source.sender().displayName,
                        Component.text(message)
                    )
                    .color(convertColorString(bot.config.colors.commands.tertiary));

            else
                component = Component
                    .translatable(
                        "[%s:%s] %s › %s",
                        Component
                            .text(bot.options.host)
                            .color(convertColorString(bot.config.colors.commands.primary)),
                        Component
                            .text(bot.options.port)
                            .color(convertColorString(bot.config.colors.commands.primary)),
                        source.sender().displayName,
                        Component.text(message)
                    )
                    .color(convertColorString(bot.config.colors.commands.tertiary));

            eachBot.chat.tellraw(
                "@a",
                component
            );
        }

        return null;
    }
}
