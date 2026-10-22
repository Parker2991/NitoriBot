package land.chipmunk.parker2991.nitoribot.nightofnights.command;

import land.chipmunk.parker2991.nitoribot.nightofnights.Bot;

public record CommandContext(Bot bot, String[] args, CommandSource source) {
}