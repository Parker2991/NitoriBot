const { fromNotch } = require('prismarine-chat')('1.20.2');
const readline = require('readline');
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
});

const CommandSource = require('../util/command_source');

module.exports = {
  data: {
    enabled: true,
    name: "console",
    type: "logging"
  },
  inject (context) {
    const bot = context.bot;
    const config = context.config;
    rl.on('line', (args) => {
      if (args.startsWith(config.console.prefix)) {
        return bot.commandManager.executeString(args.substring(config.console.prefix.length));
      } else if (args.startsWith('')) {
        bot.commandManager.executeString(`echo ${args.substring(0)}`);
      }
    });

    log = function (...args) {
      rl.output.write("\x1b[2K\r");
      console.log(args.toString());
      rl._refreshLine();
    }

    bot.on('playerChat', (message) => {
      const ansi = fromNotch(message)?.toAnsi();
      log(ansi);
    });

    bot.on('profilelessChat', (message) => {
      const ansi = fromNotch(message)?.toAnsi();
      log(ansi);
    });

    bot.on('systemChat', (message) => {
      const ansi = fromNotch(message)?.toAnsi();
      log(ansi);
    });
  }
}
