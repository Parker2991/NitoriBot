const CommandContext = require("../../command_util/command_context");

class KillCommand extends CommandContext {
  constructor() {
    super("kill", ["suicide", "quit"], "kill the bots process", 3, [""]);
  }
  execute(context) {
    const bot = context.bot;
    process.exit(1);
  }
}
module.exports = KillCommand;
