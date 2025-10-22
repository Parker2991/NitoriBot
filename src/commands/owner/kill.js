const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");

class kill extends CommandContext {
  constructor() {
    super(
      "kill",
      [
        "suicide",
        "quit"
      ],
      "kill the bots process",
      trustLevel.owner,
      [""],
      false
    );
  }
  execute(context) {
    const bot = context.bot;

    process.exit(1);
  }
}
module.exports = kill;
