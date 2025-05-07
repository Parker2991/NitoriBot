const CommandError = require("../../command_util/command_error");
const CommandContext = require("../../command_util/command_context");

class CoresettingsCommand extends CommandContext {
  constructor() {
    super("coresettings", ["cbsettings"], "change the bots core settings", 2, [
      "useplacedcommandblock <on/off/true/false/enable/disable>",
      "area start <positions>",
      "area end <positions>",
      "refillmethod/rcmethod <item/chat>",
    ]);
  }
  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const config = context.config;
    const source = context.source;
    let component = [];

    if (
      !args &&
      !args[0] &&
      !args[1] &&
      !args[2] &&
      !args[3] &&
      !args[4] &&
      !args[5]
    )
      return;
    switch (args[1]?.toLowerCase()) {
      case "useplacedcommandblock":
        switch (args[2]?.toLowerCase()) {
          case "on":
          case "true":
          case "enable":
            bot.core.usePlacedCommandBlock = true;
            component.push({
              text: "now using the placed command block",
              color: config.colors.commands.primary,
            });
            break;
          case "off":
          case "false":
          case "disable":
            bot.core.usePlacedCommandBlock = false;
            component.push({
              text: "no longer using the placed command block",
              color: config.colors.commands.primary,
            });
            break;
          default:
            if (bot.core.usePlacedCommandBlock) {
              component.push({
                text: "the bot is currently using the placed command block to run its commands",
                color: config.colors.commands.primary,
              });
            } else {
              component.push({
                text: "the bot is currently using its command block core to run its commands",
                color: config.colors.commands.primary,
              });
            }
        }
        break;
      case "area":
        switch (args[2]?.toLowerCase()) {
          case "start":
            if (isNaN(args[3]) || isNaN(args[4]) || isNaN(args[5])) {
              bot.core.area.start = config.core.area.start;
              component.push({
                text: "arguments were NaN, defaulting to config core start coords",
                color: config.colors.commands.primary,
              });
            } else {
              bot.core.area.start = {
                x: Number(args[3]),
                y: Number(args[4]),
                z: Number(args[5]),
              };
              component.push({
                text: `setting core start pos to x: ${args[3]}, y: ${args[4]}, z: ${args[5]}`,
                color: config.colors.commands.primary,
              });
            }
            break;
          case "end":
            if (isNaN(args[3]) || isNaN(args[4]) || isNaN(args[5])) {
              bot.core.area.end = config.core.area.end;
              throw new CommandError(
                "arguments were NaN, defaulting to config core end coords",
              );
            } else {
              bot.core.area.end = {
                x: Number(args[3]),
                y: Number(args[4]),
                z: Number(args[5]),
              };

              component.push({
                text: `setting core end pos to x: ${args[3]}, y: ${args[4]}, z: ${args[5]}`,
                color: config.colors.commands.primary,
              });
            }
            break;
          default:
            component.push({
              text: `core start pos: x: ${bot.core.area.start.x}, y: ${bot.core.area.start.y}, z: ${bot.core.area.start.z}, and end pos: x: ${bot.core.area.end.x}, y: ${bot.core.area.end.y}, z: ${bot.core.area.end.z}`,
              color: config.colors.commands.primary,
            });
        }
        break;
      case "refillmethod":
      case "rcmethod":
        switch (args[2]?.toLowerCase()) {
          case "item":
            config.core.itemRefill = true;
            component.push({
              text: "now refilling via item",
              color: config.colors.commands.primary,
            });
            break;
          case "chat":
            config.core.itemRefill = false;
            component.push({
              text: "now refilling via chat",
              color: config.colors.commands.primary,
            });
            break;
          default:
            if (config.core.itemRefill) {
              component.push({
                text: "currently filling core via item",
                color: config.colors.commands.primary,
              });
            } else {
              component.push({
                text: "currently filling core via chat",
                color: config.colors.commands.primary,
              });
            }
        }
        break;
      default:
        throw new CommandError("invalid argument");
    }

    if (source.sources.console) {
      bot.console.info(bot.getMessageAsPrismarine(component)?.toAnsi());
    } else {
      bot.tellraw("@a", component);
    }
  }
}

module.exports = CoresettingsCommand;
