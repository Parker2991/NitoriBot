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
          if (options.serverName !== this.server && this.server !== "all") return;

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
        let time = new Date().toLocaleTimeString("en-US", { timeZone: config.console.timezone });
        let date = new Date().toLocaleDateString("en-US", { timeZone: config.console.timezone })
        return bot.getMessageAsPrismarine(`§8[§1${time} §3${date} ${type}§8] §8[§b${server}§8]§r `)?.toAnsi() + bot.getMessageAsPrismarine(message)?.toAnsi()
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
