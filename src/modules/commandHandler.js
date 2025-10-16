const CommandSource = require("../commandUtil/CommandSource");

class commandHandler {
  constructor(context) {
    let ratelimit = 0;
    const bot = context.bot;
    const config = context.config;
    const options = context.options;

    bot.on("parsed_message", (data) => {
      try { 
        if (data.type !== "minecraft:chat" && data.type !== "extras:command_spy") return;
        const prefixes = config.prefixes;
        prefixes.map((prefix) => {
          const plainMessage = bot
            .getMessageAsPrismarine(data.contents)
            ?.toString();

          if (!plainMessage?.startsWith(prefix)) return;
          const command = plainMessage.substring(prefix.length);

          const source = new CommandSource(
            data.sender,
            { discord: false, console: false },
            data.chatType,
            data.trustLevel,
            data.validateBypass
          );

          source.sendFeedback = (message) => {
            if (options.mode === "savageFriends") bot.tellraw(`@a`, [
                message
            ])
            else bot.tellraw(`@p[nbt={UUID:[I;${source.player.entityUuid}]}]`, message)
          }
          ratelimit++;
          setTimeout(() => {
            ratelimit--;
          }, 1000);
          if (ratelimit > config.ratelimit.commands) {
            return;
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
module.exports = commandHandler;