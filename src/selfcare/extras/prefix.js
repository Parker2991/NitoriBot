class prefix {
  constructor(context) {
    const bot = context.bot;
    const options = context.options;
    const config = context.config;

    if (bot.options.mode === "savageFriends") return 
    bot.on('packet.player_info', (packet) => {
      const uuid = packet.data[0].uuid
      if (uuid === bot.uuid) {
        const player = bot.players.find((player) => player.uuid === uuid)
        const prefix = bot.getMessageAsPrismarine(player.prefix)?.toMotd()?.replaceAll('§','&')
        const forcedPrefix = `&8[&bPrefix&8: &3${config.prefixes[0]}&8] `  // i put a space here because text: ' ' is outputted at the end of player.prefix in raw json form
        if (prefix !== forcedPrefix) bot.selfcare.prefix = false
        else bot.selfcare.prefix = true
      }
    })
  }
}

module.exports = prefix;
