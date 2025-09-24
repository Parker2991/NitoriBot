const mc = require("minecraft-protocol");
const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");

class mcserver extends CommandContext {
  constructor() {
    super("mcserver",
      ["pingserver"],
      "pings minecraft servers",
      trustLevel.public,
      ["<ip>"],
    );
  }

  async execute(context) {
    const bot = context.bot;
    const config = context.config;
    const args = context.arguments;
    const source = context.source;
    const translations = bot.translations
    const [host, port] = args[0].split(':')

    let component = [];
    if (isNaN(port) && port) {
      source.sendFeedback({
        translate: "fnfboyfriendbot.arguments.invalid_integer",
        fallback: translations["fnfboyfriendbot.arguments.invalid_integer"],
        color: "red",
        with: [
          { text: "Port" }
        ]
      })
      return
    }

    process.once('uncaughtException', (error) => {
      source.sendFeedback({
        translate: "fnfboyfriendbot.command.mcserver.server.unknown",
        fallback: translations["fnfboyfriendbot.command.mcserver.server.unknown"],
        color: "red",
        with: [
          { text: `${host}` },
          { text: `${Number(port ?? 25565)}` }
        ]
      })
    })
    
    const server = await mc.ping({ host, port: Number(port ?? 25565) })

    component.push({
      translate: "%s\n%s\n%s\n%s",
      with: [
        {
          translate: "fnfboyfriendbot.command.mcserver.ip",
          fallback: translations["fnfboyfriendbot.command.mcserver.ip"],
          color: config.colors.commands.primary,
          with: [
            { text: `${host}`, color: config.colors.commands.secondary },
            { text: `${Number(port ?? 25565)}`, color: config.colors.integer }
          ]
        },
        {
          translate: "fnfboyfriendbot.command.mcserver.players",
          fallback: translations["fnfboyfriendbot.command.mcserver.players"],
          color: config.colors.commands.primary,
          with: [
            { text: `${server.players.online}`, color: config.colors.integer },
            { text: `${server.players.max}`, color: config.colors.integer }
          ]
        },
        {
          translate: "fnfboyfriendbot.command.mcserver.server.version",
          fallback: translations["fnfboyfriendbot.command.mcserver.server.version"],
          color: config.colors.commands.primary,
          with: [
            { text: `${server.version.name}`, color: config.colors.commands.secondary }
          ]
        },
        server.description
      ]
    })
    source.sendFeedback(component)
  }
}
module.exports = mcserver;
