import createRegistry from 'prismarine-registry';

export default class registry {
  constructor(context: any) {
    const bot = context.bot;
    bot.once("packet.login", (packet: any) => {
      bot.registry = createRegistry(bot._client.version);
     // bot.registry.language = require("../data/language.json");
      bot.emit("registry_ready", bot.registry);
    });


/*    bot.on('packet.registry_data', async (packet: ant) => {
      await sleep(1000)
      if (bot.loggedIn) {
        bot.registry.loadDimensionCodec(packet)
      }
    })*/
  }
}