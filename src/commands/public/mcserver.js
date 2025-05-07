const mc = require("minecraft-protocol");
const util = require("util");
const CommandContext = require("../../command_util/command_context");
class McserverCommand extends CommandContext {
  constructor() {
    super("mcserver", ["pingserver"], "pings minecraft servers", 0, ["<ip>"]);
  }

  async execute(context) {
    const bot = context.bot;
    const config = context.config;
    const args = context.arguments;
    const [host, port] = args[0].split(":");
    const source = context.source;
    const server = await mc.ping({ host, port: Number(port ?? 25565) });
    let component = [];

    try {
      component.push({
        translate: "%s: %s:%s\n%s: (%s/%s)\n%s: %s\n%s",
        color: config.colors.commands.tertiary,
        with: [
          { text: "ip", color: config.colors.commands.primary },
          { text: `${host}`, color: config.colors.commands.secondary },
          {
            text: `${Number(port ?? 25565)}`,
            color: config.colors.commands.secondary,
          },
          { text: "players", color: config.colors.commands.primary },
          { text: `${server.players.online}`, color: config.colors.integer },
          { text: `${server.players.max}`, color: config.colors.integer },
          { text: "server version", color: config.colors.commands.primary },
          {
            text: `${server.version.name}`,
            color: config.colors.commands.secondary,
          },
          server.description,
        ],
      });

      if (source.sources.console) {
        bot.console.info(bot.getMessageAsPrismarine(component)?.toAnsi());
      } else {
        bot.tellraw("@a", component);
      }
    } catch (e) {
      bot.chat.message(`${e.toString()}`);
    }
  }
};
module.exports = McserverCommand