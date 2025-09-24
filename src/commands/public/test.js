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
    );
  }

  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const source = context.source;
    
  }
}

module.exports = test;
