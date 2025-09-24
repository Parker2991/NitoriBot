const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");

class extras extends CommandContext {
  constructor() {
    super(
      "extras",
      [],
      "send messages via the extras channel",
      trustLevel.public,
      [
        "send",
        "register",
        "unregister",
        "unregisterall"
      ],
    );
  }
  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const source = context.source

    if (
      !args
      &&
      !args[0]
      &&
      !args[1]
      &&
      !args[2]
    ) return;

    switch (args[0]) {
      case "send":
        bot.extras.send(args[1], args.slice(2).join(" "))
        source.sendFeedback(`sent ${args.slice(2).join(" ")} to channel ${args[1]}`)
      break
      case "register":
        bot.extras.register(args[1])
        source.sendFeedback(`registered ${args[1]} channel`)
      break
      case "unregister":
        bot.extras.unregister(args[1])
        source.sendFeedback(`unregistered from ${args[1]} channel`)
        console.log(bot.extras.channels)
      break
      case "unregisterall":
        bot.extras.unregisterAll();
        source.sendFeedback("unregistered all channels")
      break
    }
  }
}

module.exports = extras;
