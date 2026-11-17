package land.chipmunk.parker2991.nitoribot.nightofnights.commands.Public;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandTrustLevels;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.PlayerProfileData;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CoreCommand implements CommandInfo {
    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.PUBLIC;
    }

    @Override
    public List<String> getAliases () {
        return List.of(
            "core",
            "cb",
            "corerun"
        );
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
    public Component execute (CommandContext context) {
        NitoriBot bot = context.bot();
        CommandSource source = context.source();
        String message = String.join(
            " ",
            context.args()
        );


        if (message.contains("@username") || message.contains("@uuid")) {
            for (PlayerProfileData player : bot.players.list) {

                bot.core
                    .runTrackedWithPacket(message
                        .replaceAll(
                            "\\s+@username",
                            " " + player.profile.getName()
                        )
                        .replaceAll(
                            "\\s+@uuid",
                            " " + player.profile.getIdAsString()
                        ))
                    .thenAccept(reply -> {
                        if (reply == null)
                            return;
                        source.sendFeedback(
                            bot,
                            reply
                        );
                    });
            }
        } else {
            bot.core
                .runTrackedWithPacket(message)
                .thenAccept(reply -> {
                    if (reply == null)
                        return;
                    source.sendFeedback(
                        bot,
                        reply
                    );
                });
        }

        return null;
    }
}
