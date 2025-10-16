const CommandError = require("../../commandUtil/CommandError");
const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");

class kick extends CommandContext {
  constructor() {
    super(
      "kick",
      [],
      "kick a player from the server",
      trustLevel.admin,
      ["invalidstring", "item", "te"],
      true,
    );
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

module.exports = kick;
