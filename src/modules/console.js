const CommandSource = require("../command_util/command_source");
const prismarineChat = require("prismarine-chat")("1.20.2");

class Console {
  // named it Console so that it doesnt override the console variable
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    const options = context.options;
    let ratelimit = 0;

    let serverName;

    if (options.private) {
      serverName = `${options.serverName}`;
    } else {
      serverName = `§1${bot.options.host}:§9${bot.options.port}`;
    }

    bot.console = {
      readline: null,
      server: "all",
      readlineInterface(rl) {
        this.readline = rl;
        rl.on("line", (args) => {
          if (bot.options.serverName !== this.server && this.server !== "all")
            return;
          if (args.startsWith(config.console.prefix)) {
            return bot.commandManager.executeString(
              bot.console.source,
              args.substring(config.console.prefix.length),
            );
          } else if (args.startsWith("")) {
            bot.chat.send(args);
          }
          rl.on("close", () => {
            this.readline = null;
          });
        });
      },
      /*
            data.trustLevel,
            data.validateBypass
      */
      source: new CommandSource(
        {
          profile: {
            name: bot._client.username,
          },
          uuid: bot._client.uuid,
        },
        { console: true, discord: false },
        "minecraft:chat",
        {
          trustLevel: 4,
          validateBypass: true
        }
      ),

      refreshLine(...args) {
        this.readline.output.write("\x1b[2K\r");
        console.log.apply(console, arguments);
        this.readline._refreshLine();
      },
      log(message) {
        this.refreshLine(
          bot
            .getMessageAsPrismarine(
              `§8[§1${new Date().toLocaleTimeString("en-US", { timeZone: "America/CHICAGO" })} §3${new Date().toLocaleDateString("en-US", { timeZone: "America/CHICAGO" })} §6logs§8] §8[${serverName}§8] `,
            )
            ?.toAnsi() + message,
        );
      },
      warn(error) {
        this.refreshLine(
          prismarineChat
            .fromNotch(
              `§8[§1${new Date().toLocaleTimeString("en-US", { timeZone: "America/CHICAGO" })} §3${new Date().toLocaleDateString("en-US", { timeZone: "America/CHICAGO" })} §ewarn§8] §8[${serverName}§8] `,
            )
            ?.toAnsi() + error,
        );
      },
      error(error) {
        this.refreshLine(
          bot
            .getMessageAsPrismarine(
              `§8[§1${new Date().toLocaleTimeString("en-US", { timeZone: "America/CHICAGO" })} §3${new Date().toLocaleDateString("en-US", { timeZone: "America/CHICAGO" })} §4error§8] §8[${serverName}§8] `,
            )
            ?.toAnsi() + error,
        );
      },
      info(message) {
        this.refreshLine(
          prismarineChat
            .fromNotch(
              `§8[§1${new Date().toLocaleTimeString("en-US", { timeZone: "America/CHICAGO" })} §3${new Date().toLocaleDateString("en-US", { timeZone: "America/CHICAGO" })} §2info§8] §8[${serverName}§8] `,
            )
            ?.toAnsi() + message,
        );
      },
      debug(message) {
        console.log(
          prismarineChat
            .fromNotch(
              `§8[§1${new Date().toLocaleTimeString("en-US", { timeZone: "America/CHICAGO" })} §3${new Date().toLocaleDateString("en-US", { timeZone: "America/CHICAGO" })} §6debug§8] §8[${serverName}§8] `,
            )
            ?.toAnsi() + message,
        );
      },
      command(message) {
        console.log(
          prismarineChat
            .fromNotch(
              `§8[§1${new Date().toLocaleTimeString("en-US", { timeZone: "America/CHICAGO" })} §3${new Date().toLocaleDateString("en-US", { timeZone: "America/CHICAGO" })} §6command§8] §8[${serverName}§8] `,
            )
            ?.toAnsi() +
            bot.getMessageAsPrismarine(message)?.toAnsi(bot.registry.language),
        );
      },
    };

    bot.console.source.sendFeedback = (message) => {
      bot.console.command(message);
    };
    setInterval(() => (ratelimit = 0), 1000 * 2);

    bot.on("message", (message) => {
      if (message.type === "minecraft:player_chat")
        message = [
          {
            text: "[P] ",
          },
          message.message,
        ];
      else if (message.type === "minecraft:disguised_chat") message = [
        {
          text: "[V] "
        },
        message.message
      ]
      else if (message.type === "minecraft:system_chat") message = [
        {
          text: "[S] "
        },
        message.message
      ]

      if (options.logging.console) {
        if (ratelimit > config.ratelimit.console) return;

        bot.console.log(bot.getMessageAsPrismarine(message)?.toAnsi());
      }

      if (options.logging.file && bot.loggedIn) {
        bot.console.fileLogger(
          `[${new Date().toLocaleTimeString("en-US", { timeZone: "America/CHICAGO" })} ${new Date().toLocaleDateString("en-US", { timeZone: "America/CHICAGO" })} logs] [${serverName}] ${bot.getMessageAsPrismarine(message)?.toString()}`,
        );
      }

      ratelimit++;
    });
  }
}
module.exports = Console;
