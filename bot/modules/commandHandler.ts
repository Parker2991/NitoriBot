import { CommandSource } from '../command/CommandSource';

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
        const player = packet.player

        for (const prefix of prefixes) {
          if (!plainMessage.startsWith(prefix)) return;
          const command = plainMessage.substring(prefix.length);
          if (command.length == 0) return;
          const getPrefix = plainMessage.split(command)[0]
          const source = new CommandSource(player, prefix)
          bot.commandManager.executeString(source, command)
        }
      } catch (e) {
        if (e instanceof Error) bot.console.info(e.stack)
      }
    })
  }
}
