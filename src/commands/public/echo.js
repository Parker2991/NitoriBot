const CommandContext = require("../../command_util/command_context");

class EchoCommand extends CommandContext {
  constructor() {
    super("echo", ["say", "botsay"], "make me say something!", 0, [
      "<message>",
    ]);
  }

  execute(context) {
    const bot = context.bot;
    const args = context.arguments;

    bot.chat.send(`${args.join(" ")}`);
  }
}

module.exports = EchoCommand;
