const CommandContext = require("../../command_util/command_context");
const CommandTrustLevel = require('../../command_util/command_trust_level')
const trustLevel = new CommandTrustLevel()

class validate extends CommandContext {
  constructor() {
    super("validate", ["val"], "validate through the bot", trustLevel.trusted, [""]);
  }
  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const source = context.source;
    const config = context.config;
    const { MessageBuilder } = require('prismarine-chat')(bot.registry.version)

    if (source.player.hash === "trusted") source.sendFeedback(
      new MessageBuilder()
        .setText('Valid Trusted Hash')
        .setColor(config.colors.help.trusted)
    )
    else if (source.player.hash === "admin") source.sendFeedback(
      new MessageBuilder()
        .setText('Valid Admin Hash')
        .setColor(config.colors.help.admin)
    )
    else if (source.player.hash === "owner") source.sendFeedback(
      new MessageBuilder()
        .setText('Valid Owner Hash')
        .setColor(config.colors.help.owner)
    )
  }
}

module.exports = validate
