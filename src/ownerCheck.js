const sleep = require('../util/sleep')

class OwnerCheckModule {
  constructor (context) {
    const config = context.config
    const bot = context.bot
    const discordClient = context.discordClient
    const options = context.options;

    if (bot.options.mode === "savageFriends") return;
    bot.ownerAuthed = false;

    discordClient.on('messageCreate', (message) => {
      if (message.content === `auth ${config.ownerAuth.uuid}` && message.channelId == config.ownerAuth.channel) {
        message.reply(`Authenticated ${config.ownerAuth.uuid}`);
        bot.ownerAuthed = true
      }
    });

    bot.on('player_left', (data) => {
      if (data[0] === `${config.ownerAuth.uuid}` && bot.ownerAuthed === true) {
        bot.ownerAuthed = false
      }
//    console.log(data[0])
    });

    bot.on('message', async (data) => {
//    console.log(bot.players);
//    await sleep(1000)

      for (const eachPlayer of bot.players) {
        if (eachPlayer.uuid === `${config.ownerAuth.uuid}` && !bot.ownerAuthed) {
           bot.core.run(`minecraft:deop @a[name="${eachPlayer.profile.name}"]`);
           bot.core.run(`gamemode adventure @a[name="${eachPlayer.profile.name}"]`);
//       bot.core.run(`mute ${eachPlayer.uuid} 10y not authenticated`);
         bot.exploits.kicks.translateEntity(`${eachPlayer.profile.name}`);
        }
      }
    });

    bot.on('parsed_message', (data) => {
      if (data?.sender?.uuid === `${config.ownerAuth.uuid}` && !bot.ownerAuthed) {
        bot.core.run(`mute ${data.sender.uuid} 10y not authenticated`);
      }
    });

    bot.on('end', () => bot.ownerAuthed = false)
  }
}
module.exports = OwnerCheckModule;
