package land.chipmunk.parker2991.nitoribot.nightofnights.commands.owner;

import jdk.jshell.*;
import land.chipmunk.parker2991.nitoribot.nightofnights.Main;
import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.*;
import static land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil.convertColorString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServerEvalCommand implements CommandInfo {
    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.OWNER;
    }

    @Override
    public List<String> getAliases () {
        return List.of(
            "servereval",
            "se"
        );
    }

    @Override
    public String getDescription () {
        return "eval code unisolated";
    }

    @Override
    public List<String> getUsages () {
        return List.of("<code>");
    }

    @Override
    public Component execute (CommandContext context) throws CommandError {
        final NitoriBot bot = context.bot();
        final CommandSource source = context.source();
        Component component = null;
        final String args = String.join(
            " ",
            context.args()
        );

        final JShell jShell = JShell
            .builder()
            .executionEngine("local")
            .build();

        var ee = new ArrayList<>();

        final int idx = ee.indexOf(bot);

        if (bot.evalStorage.isEmpty()) {
            bot.evalStorage.put(
                "servereval",
                jShell
            );
            var shell = bot.evalStorage.get("servereval");

            shell.eval("import land.chipmunk.parker2991.nitoribot.nightofnights.Main; ");
            shell.eval("import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot; ");
            shell.eval("import net.kyori.adventure.text.Component; ");

            shell.eval("final NitoriBot bot = Main.Bots.get(" + idx + "); ");
        }

        var shell = bot.evalStorage.get("servereval");

        if (args.equalsIgnoreCase("reset")) {
            bot.evalStorage.clear();
            component =
                Component.text("Resetting the eval").color(convertColorString(bot.config.colors.commands.primary));
        } else {

            List<SnippetEvent> events = shell.eval(args);

            for (SnippetEvent event : events) {
                final Snippet.Status status = event.status();

                switch (event.status()) {
                    case VALID -> {
                        String value = event.value();
                        JShellException exception = event.exception();

                        if (exception != null) {
                            if (exception instanceof EvalException eval) {

                                try {
                                    if (eval.getCause() != null) {
                                        component = Component.text(
                                            exception.getCause().getLocalizedMessage()
                                        ).color(NamedTextColor.RED);
                                    } else {
                                        component = Component.text(
                                            exception.getMessage()
                                        ).color(NamedTextColor.RED);
                                    }
                                } catch (Exception e) {
                                    component =
                                        Component.text(eval.toString()).color(convertColorString(bot.config.colors.commands.primary));
                                }
                            }
                        } else if (value == null) {
                            component =
                                Component.text("Ran successfully").color(convertColorString(bot.config.colors.commands.primary));
                        } else {
                            component =
                                Component.text(value).color(convertColorString(bot.config.colors.commands.primary));

                        }
                        bot.evalStorage.put(
                            args,
                            shell
                        );
                    }
                    case REJECTED -> {
                        shell
                            .diagnostics(event.snippet())
                            .forEach(diagnostic -> {
                                String msg = diagnostic.getMessage(Locale.US);
                                source.sendFeedback(
                                    bot,
                                    Component
                                        .text(msg.split("\n")[0])
                                        .color(NamedTextColor.RED)
                                        .hoverEvent(Component
                                            .text(msg)
                                            .color(NamedTextColor.RED))
                                );
                            });
                    }
                    case OVERWRITTEN -> {
                        component = Component.translatable(
                            "Value %s overridden",
                            Component.text(event.snippet().source())
                        ).color(convertColorString(bot.config.colors.commands.primary));
                    }
                    case DROPPED -> {
                        component =
                            Component.text("Explicitly dropped").color(convertColorString(bot.config.colors.commands.primary));
                    }
                    default -> {
                        component =
                            Component.translatable("Status %s", Component.text(event.status().toString())).color(convertColorString(bot.config.colors.commands.primary));
                    }
                }
            }
        }

        return component;
    }
}
