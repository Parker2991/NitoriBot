const CommandContext = require("../../command_util/command_context");

class ReconnectCommand extends CommandContext {
  constructor() {
    super("reconnect", ["end"], "reconnects the bot", 2, [""]);
  }
  execute(context) {
    const bot = context.bot;
    bot._client.end();
    bot.chat.message("reconnecting");
  }
}
module.exports = ReconnectCommand;
