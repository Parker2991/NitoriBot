const CommandContext = require("../../command_util/command_context");
const CommandError = require("../../command_util/command_error");

class ConsoleCommand extends CommandContext {
  constructor() {
    super("console", ["c"], "runs commands made for the bots console", 4, [
      "server/srv <all/servername>",
      "customchat <on/true/enable/off/false/disable>",
      "say <message>",
      "validate/validation/val <owner/o/admin/a/trusted/t>",
      "logging/togglelogging/logtoconsole <on/true/enable/off/false/disable>",
    ]);
  }
  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const source = context.source;
    const config = context.config;
    if (!args && !args[0] && !args[1] && !args[2] && !args[3]) return;
    switch (args[0]?.toLowerCase()) {
      case "server":
      case "svr":
        const servers = bot.bots.map((eachBot) => eachBot.options.serverName);
        for (const eachBot of bot.bots) {
          if (args.slice(1).join(" ").toLowerCase() === "all") {
            eachBot.console.server = "all";
            bot.console.info("Set the console server to all");
            continue;
          }
          const server = servers.find((server) =>
            server.toLowerCase().includes(args[1]),
          );
          if (!server) {
            bot.console.info("Invalid server");
            return;
          }
          bot.console.info(`Set the console server to ` + server);
          eachBot.console.server = server;
        }
        break;
      case "say":
        bot.chat.send(args.slice(1).join(" "));
        break;
      case "validate":
      case "validation":
      case "val":
        switch (args[1]?.toLowerCase()) {
          case "owner":
          case "o":
            bot.chat.message(
              `${config.prefixes[0]}${args.slice(2).shift()} ${bot.validation.owner} ${args.slice(3).join(" ")}`,
            );
            break;
          case "admin":
          case "a":
            bot.chat.message(
              `${config.prefixes[0]}${args.slice(2).shift()} ${bot.validation.admin} ${args.slice(3).join(" ")}`,
            );
            break;
          case "trusted":
          case "t":
            bot.chat.message(
              `${config.prefixes[0]}${args.slice(2).shift()} ${bot.validation.trusted} ${args.slice(3).join(" ")}`,
            );
            break;
          default:
            throw new CommandError({
              translate: "command.unknown.argument",
              color: "dark_red",
            });
        }
        break;
      case "logging":
      case "togglelogging":
      case "logtoconsole":
        switch (args[1]?.toLowerCase()) {
          case "on":
          case "enable":
          case "enabled":
          case "true":
            if (bot.options.logging === true) {
              bot.console.info(
                `logging for ${bot.options.serverName} is already enabled!`,
              );
            } else {
              bot.console.info(
                `logging for ${bot.options.serverName} is now enabled`,
              );
              bot.options.logging = true;
            }
            break;
          case "off":
          case "disable":
          case "disabled":
          case "false":
            if (bot.options.logging === false) {
              bot.console.info(
                `logging for ${bot.options.serverName} is already disabled!`,
              );
            } else {
              bot.console.info(
                `logging for ${bot.options.serverName} is now disabled`,
              );
              bot.options.logging = false;
            }
            break;
        }
        break;
      default:
        throw new CommandError({
          translate: "command.unknown.argument",
          color: "dark_red",
        });
    }
  }
}

module.exports = ConsoleCommand;
