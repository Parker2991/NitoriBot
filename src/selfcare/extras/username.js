class username {
  constructor(context) {
    const bot = context.bot;
    const options = context.options;
    const config = context.config;

    bot.on('player_info', (data) => {
      if (data.uuid === bot.uuid) {
        if (data.player.name !== bot.username) bot.selfcare.username = true;
        else bot.selfcare.username = false
      }
    })
  }
}

module.exports = username;
