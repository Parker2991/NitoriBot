const CommandSource = require("../command_util/command_source");

class command_handler {
  constructor(context) {
    let ratelimit = 0;
    const bot = context.bot;
    const config = context.config;
    const options = context.options;

    bot.on("parsed_message", (data) => {
      try {
        if (data.type !== "minecraft:chat") return;
        const prefixes = config.prefixes;
        prefixes.map((prefix) => {
          const plainMessage = bot
            .getMessageAsPrismarine(data.contents)
            ?.toMotd();

          if (!plainMessage?.startsWith(prefix)) return;
          const command = plainMessage.substring(prefix.length);
          const source = new CommandSource(
            data.sender,
            { discord: false, console: false },
            true,
          );
          ratelimit++;
          setTimeout(() => {
            ratelimit--;
          }, 1000);
          if (ratelimit > config.ratelimit.commands) {
            bot.tellraw("@a", {
              text: "You are using commands too fast!",
              color: "dark_red",
            });
          } else if (command.split(" ")[0].length === 0) {
          } else {
            bot.commandManager.executeString(source, command);
          }
        });
      } catch (e) {
        console.log(e.stack);
      }
    });
  }
}
module.exports = command_handler;
