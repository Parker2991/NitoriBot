export default class commandHandler {
  constructor (context: any) {
    const bot = context.bot;
    const config = context.config;
    const options = bot.options;

    bot.on('parsed_message', (packet: any) => {
      try {
        const prefixes = config.prefixes;
        const message = packet.contents;
        const color = message.color;
        let text;
        if (message.extra) text = message.extra[0];
        else text = message.text;
        
        //console.log(message)
     //   console.log(bot.getMessageAsPrismarine(message))
        //const e = bot.getMessageAsPrismarine(message)?.toMotd();
        //console.log(e)
      } catch (e) {
        
      }
      //console.log(packet)
    })
  }
}