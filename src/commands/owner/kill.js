const CommandContext = require("../../command_util/command_context");
const CommandTrustLevel = require("../../command_util/command_trust_level");
const trustLevel = new CommandTrustLevel();

class kill extends CommandContext {
  constructor() {
    super(
      "kill",
      ["suicide", "quit"],
      "kill the bots process",
      trustLevel.owner,
      [""],
      true,
    );
  }
  execute(context) {
    const bot = context.bot;

    process.exit(1);
  }
}
module.exports = kill;
