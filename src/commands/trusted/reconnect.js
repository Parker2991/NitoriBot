const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");

class reconnect extends CommandContext {
  constructor() {
    super(
      "reconnect",
      ["end"],
      "reconnect the bot",
      trustLevel.trusted,
      [""],
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
