const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");

class core extends CommandContext {
  constructor() {
    super(
      "core",
      [
        "cb",
        "cbrun",
        "corerun"
      ],
      "run commands in core",
      trustLevel.public,
      [
        "$username <message>",
        "$uuid <message>",
        "<message>"
      ],
    );
  }
  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const source = context.source;

    if (args.includes("$username") || args.includes("$uuid")) {
      bot.players.forEach((eachPlayer) => {
        bot.core.runTracked(
          args
            .join(" ")
            .replaceAll("$username", eachPlayer.profile.name)
            .replaceAll("$uuid", eachPlayer.uuid),
          source,
        );
      });
    } else if (bot.options.mode === "savageFriends") {
      bot.core.run(args.join(" "));
    } else {
      bot.core.runTracked(args.join(" "), source);
    }
  }
}

module.exports = core;
