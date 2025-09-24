const fs = require("fs");
const path = require("path");
const CommandError = require("../command_util/command_error");
const CommandSource = require("../command_util/command_source");
const CommandArguments = require('../command_util/command_arguments')
const fixansi = require("../util/ansi");
const sleep = require("../util/sleep.js");

function unknownCommand(commandName, bot) {
  return {
    translate: "%s%s%s %s",
    color: "red",
    with: [
      { translate: "command.unknown.command", color: "red" },
      { text: "\n"},
      { text: `${commandName}`, color: "gray" },
      { translate: "command.context.here", color: "red" }
    ]
  } 
}

class command_manager {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    const discordClient = context.discordClient;
    const options = context.options;
    const translations = bot.translations;

    bot.commandManager = {
      commands: {},
      commandlist: [],
      execute(source, commandName, args) {
        const command = this.getCommand(commandName.toLowerCase());
        try {
          const authFind = bot.auth.list.find((e) => e.player === source.player.uuid)
          if (!command) {
            throw new CommandError(
              unknownCommand(commandName, bot),
            );
          }

          if (args.length > command.data.args) throw new CommandError({
            translate: "fnfboyfriendbot.arguments",
            fallback: translations["fnfboyfriendbot.arguments"],
            color: "red",
            with: [
              { text: `${command.data.args}` },
              { text: `${args.length}`}
            ]
          })

          if (authFind) {
            source.player.validateBypass = true
            if (authFind.trustLevel === "trusted") source.player.trustLevel = 1;
            else if (authFind.trustLevel === "admin") source.player.trustLevel = 2;
            else if (authFind.trustLevel === "owner") source.player.trustLevel = 3;
          }

          if (command?.data?.trustLevel !== 0 && command?.data.trustLevel < 4) {
            if (!source.player.validateBypass && !source.sources.console && !source.sources.discord) {
              if (args[0] === bot.validation.trusted)
                source.player.trustLevel = 1
              else if (args[0] === bot.validation.admin)
                source.player.trustLevel = 2
              else if (args[0] === bot.validation.owner)
                source.player.trustLevel = 3

              if (args[0] === bot.validation.discord.trusted) {
                source.player.trustLevel = 1
                bot.validation.discord.trusted = undefined;
              } else if (args[0] === bot.validation.discord.admin) {
                source.player.trustLevel = 2;
                bot.validation.discord.admin = undefined;
              } else if (args[0] === bot.validation.discord.owner) {
                console.log(args[0])
                console.log(bot.validation.discord.owner)
                source.player.trustLevel = 3
                bot.validation.discord.owner = undefined
              }
              args = args.slice(1)
              if (source.player.trustLevel < command?.data?.trustLevel && !source.sources.console) {
                throw new CommandError({
                  translate: "fnfboyfriendbot.commandManager.validation.invalid_hash",
                  fallback: translations["fnfboyfriendbot.commandManager.validation.invalid_hash"],
                  color: "red"
                })
              }
            } if (source.sources.discord) {
              const event = bot.discord.message;
              const roles = event?.member?.roles?.cache;
              const hashRole = roles?.some((role) => {
                if (role.name === `${config.discord.roles.trusted}`) source.player.trustLevel = 1;
                else if (role.name === `${config.discord.roles.admin}`) source.player.trustLevel = 2;
                else if (role.name === `${config.discord.roles.owner}`) source.player.trustLevel = 3;
              })

              if (source.player.trustLevel < command?.data?.trustLevel) 
                throw new CommandError({
                  translate: "fnfboyfriendbot.commandManager.validation.invalid_role",
                  fallback: translations["fnfboyfriendbot.commandManager.validation.invalid_role"],
                  color: "red"
                })
            }
          } if (command?.data?.trustLevel === 4 && !source.sources.console)
            throw new CommandError({
              translate: "fnfboyfriendbot.commandManager.trustLevel.consoleOnly",
              fallback: translations["fnfboyfriendbot.commandManager.trustLevel.consoleOnly"],
              color: "red"
            })

          if (
            command?.data.playerChat
            &&
            source.ChatType !== "minecraft:player_chat"
            &&
            !source.sources.console
            && 
            !source.sources.discord
            && 
            bot.options.mode === 'kaboom'
          ) return
          return command?.execute({
            bot,
            source,
            arguments: args,
            config,
            discordClient,
          });

        } catch (error) {
          if (error instanceof CommandError) {
            source.sendFeedback(error._message);
          } else {
            bot.console.error(error.stack)
            source.sendFeedback(
              {
                translate: "command.failed",
                color: "red",
                hoverEvent: {
                  action: "show_text",
                  contents: String(error.stack),
                },
              }
            )
          }
        }
      },

      executeString(source, command) {
        const [commandName, ...args] = command.split(" ");

        return this.execute(source, commandName, args);
      },

      register(command) {
        this.commands[command.data.name] = command;
        if (command.data.aliases) {
          command.data.aliases.map((a) => (this.commands[a] = command));
        }
      },

      getCommand(name) {
        return this.commands[name];
      },

      getCommands() {
        return Object.values(this.commands);
      },
    };

    /*
      command loader ported from my discord bot SkiBot
    */
    let commandlist = [];
    const foldersPath = path.join(__dirname, "../commands");
    const commandFolders = fs.readdirSync(foldersPath);
    for (const folder of commandFolders) {
      const commandsPath = path.join(foldersPath, folder);
      const commandFiles = fs.readdirSync(commandsPath);
      for (const filename of commandFiles) {
        try {
          const filePath = path.join(commandsPath, filename);

          if (filename.endsWith(".js")) {
            const command = require(filePath);
            const commands = new command();
            bot.commandManager.register(commands);
            bot.commandManager.commandlist.push(commands);
          }
        } catch (error) {
          console.error("Failed to load command ", filename, ":", error);
        }
      }
    }
  }
}
module.exports = command_manager;
