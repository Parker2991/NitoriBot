const CommandContext = require("../../command_util/command_context");
const CommandTrustLevel = require('../../command_util/command_trust_level')
const trustLevel = new CommandTrustLevel()

class refillcore extends CommandContext {
  constructor() {
    super("rc", ["refill", "refillcore"], "refill the bots core", trustLevel.public, [
      '',
    ],
    true
    );
  }

  execute(context) {
    const bot = context.bot;
    const args = context.arguments;

    bot.core.move(bot.position)
    bot.tellraw("@a", 'refilling core')
  }
}

module.exports = refillcore;
