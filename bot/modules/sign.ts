const convertNbtToComponent = require('../util/convertNbtComponentToJson');

export class sign {
  constructor (context: any) {
    const bot = context.bot;
    const options = bot.options;

    bot.on('packet.tile_entity_data', (packet: any) => {
      const front = convertNbtToComponent(null, packet.nbtData.value.front_text);
      const back = convertNbtToComponent(null, packet.nbtData.value.back_text);
      console.log(front);
      console.log(back);
    });

    bot.on("packet.error", (error: any) => console.error(error))
  }
};
