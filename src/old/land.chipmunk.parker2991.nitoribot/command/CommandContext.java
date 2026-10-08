package land.chipmunk.parker2991.nitoribot.command;

import land.chipmunk.parker2991.nitoribot.Bot;

public record CommandContext(Bot bot, String[] args, CommandSource source) {
}