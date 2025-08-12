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
            null
          );

          source.sendFeedback = (message) => {
            if (source.sources.console) bot.console.command(message);
            else if (source.sources.discord) {
            // i dont know what to do for discord yet
            } else bot.tellraw(`@a[name="${source.player.profile.name}"]`, message);
          }

          if (command.split(' ')[1] === bot.validation.trusted) source.player.hash = "trusted";
          else if (command.split(' ')[1] === bot.validation.admin) source.player.hash = "admin";
          else if (command.split(' ')[1] === bot.validation.owner) source.player.hash = "owner";
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
