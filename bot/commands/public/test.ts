import { CommandContext } from '../../command/CommandContext';

export default class test extends CommandContext {
  constructor() {
    super(
      ['te'],
      "testing stuff",
      []
    )
  }

  execute (context: any) {
    const bot = context.bot;
    const config = context.config;
    const args = context.arguments;

    bot.chat.send('meow :3')
  }
}
