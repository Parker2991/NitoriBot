const CommandContext = require("../../command_util/command_context");
const CommandTrustLevel = require("../../command_util/command_trust_level");
const trustLevel = new CommandTrustLevel();

class validate extends CommandContext {
  constructor() {
    super(
      "validate",
      ["val"],
      "validate through the bot",
      trustLevel.trusted,
      [""],
      false,
    );
  }
  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const source = context.source;
    const config = context.config;
    let component = []

    if (source.player.hash === "trusted")
      component.push({
        text: "Valid Trusted Hash",
        color: config.colors.help.trusted
      })

    else if (source.player.hash === "admin")
      component.push({
        text: "Valid Admin Hash",
        color: config.colors.help.admin
      })
    else if (source.player.hash === "owner")
      component.push({
        text: "Valid Owner Hash",
        color: config.colors.help.owner
      })

    source.sendFeedback(component)
  }
}

module.exports = validate;
