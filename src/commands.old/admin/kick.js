const CommandError = require("../../command_util/command_error");
const CommandContext = require("../../command_util/command_context");

class KickCommand extends CommandContext {
  constructor() {
    super("kick", [], "kick a player from the server", 2, [
      "invalidstring",
      "item",
      "te",
    ]);
  }
  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const config = context.config;

    if (!args && !args[0] && !args[1] && !args[2]) return;
    switch (args[1]?.toLowerCase()) {
      case "invalidstring":
        bot.exploits.crashes.invalidString(args[2]);
        bot.tellraw("@a", `crashing ${args[2]}`);
        break;
      case "item":
        bot.exploits.kicks.item(args[2]);
        bot.tellraw("@a", `kicking ${args[2]}`);
        break;
      case "te":
        bot.exploits.kicks.translateEntity(args[2]);
        bot.tellraw("@a", `kicking ${args[2]}`);
        break;
      default:
        throw new CommandError("invalid argument");
    }
  }
}

module.exports = KickCommand;
