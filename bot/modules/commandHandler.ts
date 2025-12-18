import { source } from '../command/source';

export class commandHandler {
  constructor (context: any) {
    const bot = context.bot;
    const config = context.config;
    const options = bot.options;

    bot.on('parsed_message', (packet: any) => {
      try {
        const prefixes = config.prefixes;
        const message = packet.contents;
        const plainMessage = bot.getMessageAsPrismarine(message)?.toString();

        for (const prefix of prefixes) {
          if (!plainMessage.startsWith(prefix)) return;
          const command = plainMessage.substring(prefix.length);
          const getPrefix = plainMessage.split(command)[0];
          bot.core.run(command)
        }
      } catch (e) {
        if (e instanceof Error) bot.console.info(e.stack)
      }
    })
  }
}
