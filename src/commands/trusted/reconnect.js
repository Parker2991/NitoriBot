const CommandContext = require("../../command_util/command_context");
const CommandTrustLevel = require("../../command_util/command_trust_level");
const trustLevel = new CommandTrustLevel();

class reconnect extends CommandContext {
  constructor() {
    super(
      "reconnect",
      ["end"],
      "reconnect the bot",
      trustLevel.trusted,
      [""],
      true,
    );
  }
  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const source = context.source;
    const config = context.config;

    bot._client.end();
  }
}

module.exports = reconnect;
