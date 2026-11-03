package land.chipmunk.parker2991.nitoribot.nightofnights.commands.Public;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
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
    public void execute (CommandContext context) {
        NitoriBot bot = context.bot();
        CommandSource source = context.source();
        String message = String.join(
            " ",
            context.args()
        );

        Component output = bot.core.runTrackedWithPacket(message);

        try {
            System.out.println(output);
            source.sendFeedback(
                bot,
                output
            );
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        /*if (message.contains("@username") || message.contains("@uuid")) {
            for (PlayerProfileData player : bot.players.list) {
                Component output = bot.core.runTrackedWithTellraw(
                    "nitoribot.command_block.output",
                    "@a",
                    message.replaceAll(
                        "\\s+@username",
                        " " + player.profile.getName()
                    ).replaceAll(
                        "\\s+@uuid",
                        " " + player.profile.getIdAsString()
                    )
                );
                source.sendFeedback(bot, output);
            }
        } else {

            //bot.core.runTrackedWithTellraw("nitoribot.command_block.output", "@a", message);
            //source.sendFeedback(bot, output);
        }*/
        /*CompletableFuture.runAsync(() -> {
            source.sendFeedback(bot, bot.core.output);
            bot.core.output = null;
        }, CompletableFuture.delayedExecutor(100, TimeUnit.MILLISECONDS));*/
    }//if (args.includes("$username") || args.includes("$uuid")) {
}
