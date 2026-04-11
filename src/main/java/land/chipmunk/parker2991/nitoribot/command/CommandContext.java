package land.chipmunk.parker2991.nitoribot.command;

import land.chipmunk.parker2991.nitoribot.Bot;

public class CommandContext {
  public Bot bot;

  public String[] args;

  public CommandSource source;

  public CommandInfo commandData;

  public CommandContext (Bot bot, String[] args, CommandSource source, CommandInfo commandData) {
    this.bot = bot;
    this.args = args;
    this.source = source;
    this.commandData = commandData;
  }
}