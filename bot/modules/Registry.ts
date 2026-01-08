import createRegistry from 'prismarine-registry';
import language from '../resources/Language.json';

export default class Registry {
  constructor(context: any) {
    const bot = context.bot;

    bot.registry = createRegistry(bot.options.version);
    bot.registry.language = language;
    bot.emit("registry_ready", bot.registry);

    bot.on('packet.registry_data', (packet: any) => {
      bot.registry.loadDimensionCodec(packet)
    })
  }
}
