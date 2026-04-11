package land.chipmunk.parker2991.nitoribot.commands.Public;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandTrustLevels;

public class EchoCommand extends CommandInfo {
  public EchoCommand () {
    super(
      "echo",
      CommandTrustLevels.PUBLIC,
      new String[] { "say" },
      "make the bot say a message"
    );
  }

  @Override
  public CommandContext execute(CommandContext context) {
    Bot bot = context.bot;
    String args = String.join(" ", context.args);//context.args.join(' ');

    if (args.startsWith("/")) {
      bot.chat.command(args.substring("/".length()));
//      return;
    } else {
      bot.chat.message(args);
    }
    //CommandSource source = context.source;

    return null;
  }
}