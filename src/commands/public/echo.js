const CommandContext = require("../../command_util/command_context");
const CommandTrustLevel = require('../../command_util/command_trust_level')
const trustLevel = new CommandTrustLevel()

class echo extends CommandContext {
  constructor() {
    super("echo", ["say", "botsay"], "make me say something!", trustLevel.public, [
      "<message>",
    ]);
  }

  execute(context) {
    const bot = context.bot;
    const args = context.arguments;

    bot.chat.send(`${args.join(" ")}`);
  }
}

module.exports = echo;
