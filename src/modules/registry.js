const registry = require('prismarine-registry');

module.exports = {
  data: {
    description: "registers the client data",
    name: 'registry',
    type: 'client',
    enabled: true
  },
  inject (context) {
    const bot = context.bot;
    bot.on('login', packet => {
      bot.registry = registry(bot._client.version)
      bot.registry.language = require('../data/language.json');
      bot.emit('registry_ready', bot.registry)
    });
  }

}
