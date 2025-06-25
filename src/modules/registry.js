const registry = require('prismarine-registry');

module.exports = {
  data: {
    description: "registers the client data",
    enabled: true,
    name: 'registry',
    type: 'client',
  },
  inject (context) {
    const bot = context.bot;
    bot.on('packet.login', packet => {
      bot.registry = registry(bot._client.version)
//      bot.registry.language = require('../data/language.json');
      bot.emit('registry_ready', bot.registry)
    });
  }

}
