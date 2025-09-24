const CommandContext = require("../../command_util/command_context");
const CommandTrustLevel = require("../../command_util/command_trust_level");
const trustLevel = new CommandTrustLevel();
const CommandArguments = require('../../command_util/command_arguments');

class test extends CommandContext {
  constructor() {
    super(
      "test",
      [],
      "testing stuff",
      trustLevel.public,
      [""],
      false,
      1
    );
  }

  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const source = context.source;
    source.sendFeedback(CommandArguments.getMaxArguments(this.data.maxArgs, args.join(' ')))
  }
}

module.exports = test;
