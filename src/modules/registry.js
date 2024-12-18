const registry = require('prismarine-registry');

module.exports = {
  data: {
    name: 'registry',
    type: 'client',
    enabled: true
  },
  inject (context) {
    let bot = context.bot;
    bot.on('login', packet => {
      bot.registry = registry(bot.version)
      bot.registry.language = require('../data/language.json');
      bot.emit('registry_ready', bot.registry)
    });
  }

}
