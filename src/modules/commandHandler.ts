export default class commandHandler {
  constructor (context: any) {
    const bot = context.bot;
    const config = context.config;
    const options = bot.options;

    bot.on('parsed_message', (packet: any) => {
      console.log(packet)
    })
  }
}