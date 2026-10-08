package land.chipmunk.parker2991.nitoribot.commands.Public;

import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandTrustLevels;

import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;


/*
Adventure Minimessage is very fucking weird.....
the fact it uses an HTML style with strings instead of components is very weird
it also makes the JSON so fucking big especially with gradients
this does remind me of how i did component.push shit in the JS build of my bot aka v8.0.0-Reignite
 */
public class TestCommand implements CommandInfo {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public TestCommand () {

    }

    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.PUBLIC;
    }

    @Override
    public List<String> getAliases () {
        return List.of("test");
    }

    @Override
    public String getDescription () {
        return "test cmd";
    }

    @Override
    public void execute (CommandContext context) {
        var bot = context.bot();
        var args = String.join(" ", context.args());

        var source = context.source();

        var component = MINI_MESSAGE.deserialize(String.format(
            "%s%s %s %s %s %s %s%s%s",
            "<gradient:dark_aqua:aqua:blue:dark_blue:light_purple:dark_purple>", // gradient shenanigans
            "Hello World! User:",
            source.sender().profile().getName(),
            "UUID:",
            source.sender().uuid(),
            "Args:",
            "<rainbow>", // we gotta turn the arguments gay!
            args,
            "</gradient>"
        ));

        source.sendFeedback(bot, component);
    }
}