const CommandError = require("../../command_util/command_error");
const fixAnsi = require("../../util/ansi");
const CommandContext = require("../../command_util/command_context");

class CloopCommand extends CommandContext {
  constructor() {
    super("cloop", ["commandloop", "loop"], "loops commands", 1, [
      "add <interval> <command>",
      "remove <index>",
      "list",
      "clear",
    ]);
  }
  execute(context) {
    const bot = context.bot;
    const config = context.config;
    const source = context.source;
    let args;
    let component = [];

    if (!source.sources.console && !source.sources.discord) {
      args = context.arguments.slice(16);
    } else {
      args = context.arguments;
    }

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

    switch (args[0]?.toLowerCase()) {
      case "add":
        if (isNaN(args[1])) throw new CommandError("invalid interval");

        const interval = parseInt(args[1]);
        const command = args.slice(2).join(" ");
        bot.cloop.add(command, interval);

        component.push(
          `added ${command} to the cloops with the interval ${interval}`,
        );
        break;
      case "clear":
        bot.cloop.clear();
        component.push("cleared the cloops");
        break;
      case "remove":
        if (isNaN(args[1]))
          throw new CommandError("argument must be an integer!");

        const index = parseInt(args[1]);
        bot.cloop.remove(index);

        component.push(`removed ${index} from cloops`);
        break;
      case "list":
        let listComponent = [];
        let i = 0;
        for (const cloop of bot.cloop.list) {
          listComponent.push({
            translate: "%s \u203a %s (%s)",
            color: config.colors.commands.primary,
            with: [
              { text: `${i}`, color: config.colors.integer },
              cloop.command,
              { text: `${cloop.interval}`, color: config.colors.integer },
            ],
          });
          listComponent.push("\n");
          i++;
        }

        listComponent.pop();

        component.push({
          translate: "Cloops (%s):",
          color: config.colors.commands.primary,
          with: [
            { text: `${bot.cloop.list.length}`, color: config.colors.integer },
          ],
        });
        component.push("\n");

        if (bot.cloop.list.length > 0) {
          component.push(listComponent);
        }
        break;
      default:
        throw new CommandError("invalid argument");
    }

    if (source.sources.discord) {
    } else if (source.sources.console) {
      bot.console.info(component);
    } else {
      bot.tellraw("@a", component);
    }
  }
}

module.exports = CloopCommand;
