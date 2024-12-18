const { fromNotch } = require('prismarine-chat')('1.20.2');
const readline = require('readline');
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
});

module.exports = {
  data: {
    description: "logs chat to console",
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
        if (bot.customChat.enabled) {
          if (args.startsWith('/')) {
            bot.chat(`/${args.substring(1)}`);
            return;
          }
          bot.customChat.chat(args.substring(0));
        } else {
          bot.commandManager.executeString(`echo ${args.substring(0)}`);
        }
      }
    });

    refreshLine = function (...args) {
      rl.output.write("\x1b[2K\r");
      console.log(args.toString());
      rl._refreshLine();
    }

    bot.console = {
      log (message) {
        refreshLine(`${fromNotch("§8[§6logs§8]§r")?.toAnsi()} ${fromNotch(message)?.toAnsi(require('../data/language.json'))}`)
      },
      command (message) {
        refreshLine(`${fromNotch("§8[§ecommand§8]§r")?.toAnsi()} ${fromNotch(message)?.toAnsi(require('../data/language.json'))}`)
      },
      disconnect (reason) {
        refreshLine(`${fromNotch("§8[§bdisconnect§8]§r")?.toAnsi()} ${fromNotch(message)?.toAnsi(require('../data/language.json'))}`)
      },
      warn (error) {
        refreshLine(`${fromNotch("§8[§cwarn§8]§r")?.toAnsi()} ${fromNotch(message)?.toAnsi(require('../data/language.json'))}`)
      },
      error (error) {
        refreshLine(`${fromNotch("§8[§4error§8]§r")?.toAnsi()} ${fromNotch(message)?.toAnsi(require('../data/language.json'))}`)
      }
    }

    bot.on('playerChat', (message) => {
      bot.console.log(message);
    });

    bot.on('profilelessChat', (message) => {
      bot.console.log(message);
    });

    bot.on('systemChat', (message) => {
      bot.console.log(message);
    });
  }
}
