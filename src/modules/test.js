const registry = require('prismarine-registry');

module.exports = {
  data: {
    name: 'test',
    type: 'client',
    enabled: false
  },
  inject (context) {
    let bot = context.bot;
    bot.end();
/*    bot.on('login', packet => {
      bot.registry = registry(bot.version)
      bot.registry.language = require('../data/language.json');
      bot.emit('registry_ready', bot.registry)
    });*/
  }

}
