const CommandContext = require("../../command_util/command_context");

class RefillcoreCommand extends CommandContext {
  constructor() {
    super("refillcore", ["rc", "refill"], "refill the bots core", 0, [""]);
  }
  execute(context) {
    const bot = context.bot;
    const config = context.config;
    bot.core.move(bot.position);
    bot.tellraw("@a", "Refilling core,...");
  }
}
module.exports = RefillcoreCommand;
