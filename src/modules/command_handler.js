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
            ?.toString();

          if (!plainMessage?.startsWith(prefix)) return;
          const command = plainMessage.substring(prefix.length);
          
          const source = new CommandSource(
            data.sender,
            { discord: false, console: false },
            data.chatType,
            null,
          );

          source.sendFeedback = (message) => {
            if (options.mode === "savageFriends") bot.tellraw(`@a[name=${source.player.profile.name}]`, [
                message
            ])
            else bot.tellraw(`@a[name="${source.player.profile.name}"]`, message)
          }

          if (command.split(" ")[1] === bot.validation.trusted)
            source.player.hash = "trusted";
          else if (command.split(" ")[1] === bot.validation.admin)
            source.player.hash = "admin";
          else if (command.split(" ")[1] === bot.validation.owner)
            source.player.hash = "owner";
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

    bot.on('packet.custom_payload', (packet) => {
      if (packet.channel === "extras:message") {
        try {
          const decoded = packet.data.toString('ascii');
          const prefixes = config.prefixes
          const string = decoded.substring(20)
          const channel = packet.data.toString('ascii').split('\x05')[0]
          if (channel === config.skibot.channel) return
          const parsed = JSON.parse(string)
          const args = parsed.message
          const uuid = parsed.player
          prefixes.map((prefix) => {
            if (!args?.startsWith(prefix)) return;
            const command = args.substring(prefix.length)
            const sender = bot.players.find((e) => e.uuid === uuid)

            const source = new CommandSource(
              sender,
              { discord: false, console: false },
              'extras:message',
              null,
            )
            console.log(source.player.profile.name)
            source.sendFeedback = (message) => {
              bot.tellraw(`@a[name="${source.player.profile.name}"]`, message)
            }
            bot.commandManager.executeString(source, command)
          })
        } catch (e) {
          bot.console.error(e.toString())
        }
      }
    })
  }
}
module.exports = command_handler;