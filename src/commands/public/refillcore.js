const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");

class refillcore extends CommandContext {
  constructor() {
    super(
      "rc",
      [
        "refill",
        "refillcore"
      ],
      "refill the bots core",
      trustLevel.public,
      [""],
    );
  }

  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const source = context.source

    bot.core.move(bot.position);

    source.sendFeedback({
      translation: "command.core.refill",
    })  // most likely wont be seen most of the time
  }
}

module.exports = refillcore;
