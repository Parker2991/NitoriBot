const sleep = require("../../util/sleep");
const CommandContext = require("../../command_util/command_context");

class core extends CommandContext {
  constructor() {
    super("core", ["cb", "cbrun", "corerun"], "run commands in core", 0, [
      "$username <message>",
      "$uuid <message>",
      "<message>",
    ]);
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
        //        await sleep(1000)
      });
    } else if (bot.options.mode === "savageFriends") {
      bot.core.run(args.join(" "));
    } else {
      bot.core.runTracked(args.join(" "), source);
    }
  }
}

module.exports = core;
