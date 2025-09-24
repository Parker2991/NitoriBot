const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");

class echo extends CommandContext {
  constructor() {
    super(
      "echo",
      [
        "say",
        "botsay"
      ],
      "make me say something!",
      trustLevel.public,
      [""],
    );
  }

  execute(context) {
    const bot = context.bot;
    const args = context.arguments;

    bot.chat.send(`${args.join(" ")}`);
  }
}

module.exports = echo;
