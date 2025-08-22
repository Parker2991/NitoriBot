const CommandContext = require("../../command_util/command_context");

class NetmsgCommand extends CommandContext {
  constructor() {
    super(
      "netmsg",
      ["networkmessage", "netmessage"],
      "send a message to other servers",
      0,
      ["<message>"],
    );
  }

  execute(context) {
    const bot = context.bot;
    const config = context.config;
    const args = context.arguments;
    const source = context.source;

    let component = [];

    if (bot.options.private === true) {
      component.push({
        translate: "[%s] %s \u203a %s",
        color: config.colors.commands.tertiary,
        with: [
          {
            text: `${bot.options.serverName}`,
            color: config.colors.commands.primary,
          },
          source?.player?.displayName ?? source?.player?.profile?.name,
          {
            text: `${args.join(" ")}`,
            color: config.colors.commands.secondary,
          },
        ],
      });
    } else {
      component.push({
        translate: "[%s:%s] %s \u203a %s",
        color: config.colors.commands.tertiary,
        with: [
          {
            text: `${bot.options.host}`,
            color: config.colors.commands.primary,
          },
          {
            text: `${bot.options.port}`,
            color: config.colors.commands.primary,
          },
          source.player.displayName ?? source.player.profile.name,
          {
            text: `${args.join(" ")}`,
            color: config.colors.commands.secondary,
          },
        ],
      });
    }

    for (const eachBot of bot.bots) {
      eachBot.tellraw("@a", component);
    }
  }
}
module.exports = NetmsgCommand;
