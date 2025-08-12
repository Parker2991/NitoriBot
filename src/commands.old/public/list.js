const CommandError = require("../../command_util/command_error");
const CommandContext = require("../../command_util/command_context");

class ListCommand extends CommandContext {
  constructor() {
    super("list", ["pl", "playerlist"], "check whos online on the server", 0, [
      "",
    ]);
  }
  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const config = context.config;
    const source = context.source;

    let component = [];
    let infoComponent = [];
    let playerCount = [];

    playerCount.push({
      translate: "%s: (%s)\n",
      color: config.colors.commands.tertiary,
      with: [
        { text: "Players", color: config.colors.commands.primary },
        { text: `${bot.players.length}`, color: config.colors.integer },
      ],
    });

    component.push(playerCount);

    for (const player of bot.players) {
      component.push({
        translate: "%s \u203a %s [%s: %s]",
        color: config.colors.commands.tertiary,
        with: [
          player.displayName ?? player.profile.name,
          { text: `${player.uuid}`, color: config.colors.commands.primary },
          { text: "Latency", color: config.colors.commands.primary },
          { text: `${player.latency}`, color: config.colors.integer },
        ],
      });
      component.push("\n");
    }
    component.pop();

    if (source.sources.console) {
      bot.console.info(bot.getMessageAsPrismarine(component)?.toAnsi());
    } else {
      bot.tellraw("@a", component);
    }
  }
}

module.exports = ListCommand;
