package land.chipmunk.parker2991.nitoribot.nightofnights.commands.Public;

import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandTrustLevels;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.cloudburstmc.math.vector.Vector3i;

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
    public List<String> getUsages () {
        return null;
    }

    @Override
    public void execute (CommandContext context) {
        var bot = context.bot();
        String[] args = context.args();

        var source = context.source();

        bot.world.getBlock(Vector3i.from(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])));
    }
}