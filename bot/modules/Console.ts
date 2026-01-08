import translations from '../resources/Translations.json';
import format from '../data/console/format';
export default class Console {
  constructor (context: any) {
    const bot = context.bot;
    const config = context.config;
    const options = bot.options;
    let host = `${options.host}:${options.port}`;
    
    bot.console = {
      readline: null,

      currentServer: "all",

      interface(rl: any) {
        this.readline = rl;

        rl.on("line", (args: any) => {
          console.log(args)
          //if (options.serverName !== this.server && this.server !== "all") return;

          if (args.startsWith(config.console.prefix)) {

          } else {
            bot.chat.send(args)
          }
        });

        rl.on("close", () => {
          this.readline = null;
        });
      },

      refreshLine(...args: any) {
        this?.readline?.output.write("\x1b[2K\r");
        console.log.apply(console, args);
        this?.readline?._refreshLine();
      },

      prefix(type: any, server: any, message: any) {
        // i read the firefox docs to see how to get elements of date seperately if that makes any sense
        const timeZone = config.console.timeZone
        const dayName = new Date().toLocaleDateString('en-US', { weekday: 'long', timeZone });
        const dayNumber = new Date().toLocaleDateString('en-US', { day: 'numeric', timeZone });
        const month = new Date().toLocaleDateString('en-US', { month: 'long', timeZone });
        const year = new Date().toLocaleDateString('en-US', { year: 'numeric', timeZone });
        //const date = format(translations['console.format.date'], `${dayName}`, `${month}`, `${dayNumber}`, `${year}`);
        const time = new Date().toLocaleTimeString("en-US", { timeZone });
        const consoleFormat = format(bot, time, dayName, month, dayNumber, year, type, server, message);

        return bot.getMessageAsPrismarine(consoleFormat)?.toAnsi()
      },

      log(message: any) {
        this.refreshLine(
          this.prefix('§6log', host, message)
        )
      },

      info(info: any) {
        this.refreshLine(
          this.prefix('§6info', host, info)
        )
      }
    }

    bot.on('message', (data: any) => {
      const message = data.message;
      bot.console.log(message)
    });
  }
}
