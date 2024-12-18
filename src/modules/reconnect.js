const mc = require('minecraft-protocol');

module.exports = {
  data: {
    enabled: true,
    name: "reconnect",
    type: "client"
  },
  inject (context) {
    const bot = context.bot;
    const config = context.config;
    if (bot.server.reconnectDelay < 0) return
    bot.on('end', () => {
      setTimeout(() => {
        let client = new mc.createClient(bot.server)
        bot._client = client;
        bot.emit('init_client', client)
      }, bot.server.reconnectDelay);
    })
  }
}
