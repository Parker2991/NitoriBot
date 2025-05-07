const createRegistry = require("prismarine-registry");

class RegistryModule {
  constructor(context) {
    const bot = context.bot;
    bot.on("packet.login", (packet) => {
      bot.registry = createRegistry(bot._client.version);
      bot.registry.language = require("../data/language.json");
      bot.emit("registry_ready", bot.registry);
    });
  }
}
module.exports = RegistryModule;
