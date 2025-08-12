const CommandContext = require("../../command_util/command_context");

class ValidateCommand extends CommandContext {
  constructor() {
    super("validate", ["val"], "validate through the bot", 1, [""]);
  }
  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const source = context.source;

    if (args[0] === bot.validation.trusted) {
      bot.chat.message("&2Valid Trusted hash");
    }
    if (args[0] === bot.validation.admin) {
      bot.chat.message("&2Valid Admin hash");
    }
    if (args[0] === bot.validation.owner) {
      bot.chat.message("&2Valid Owner hash");
    }
  }
}

module.exports = ValidateCommand;
