package land.chipmunk.parker2991.nitoribot.nightofnights.command;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;

public record CommandContext(NitoriBot bot, String[] args, CommandSource source) {
}