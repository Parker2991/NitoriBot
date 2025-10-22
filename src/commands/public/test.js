const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");

class test extends CommandContext {
  constructor() {
    super(
      "test",
      [],
      "testing stuff",
      trustLevel.public,
      [""],
      true
    );
  }

  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const source = context.source;

    bot.chat.message('meow')
  }
}

module.exports = test;
