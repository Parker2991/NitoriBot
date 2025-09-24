class death {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    const options = context.options;

    bot.on('packet.update_health', (data) => { // had to add this because some idiot kept killing the bot in savageFriends
      if (data.health == 0) bot._client.write('client_command', { action: 0 })
    })
  }
}
module.exports = death