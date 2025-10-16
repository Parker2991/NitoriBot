const fs = require("fs");
const path = require("path");
const CommandError = require("../commandUtil/errors/CommandError");
const UnknownCommand = require('../commandUtil/errors/UnknownCommand')
const CommandSource = require("../commandUtil/CommandSource");
const CommandArguments = require('../commandUtil/CommandArguments')
const fixansi = require("../util/ansi");
const sleep = require("../util/sleep.js");

class commandManager {
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
            throw new UnknownCommand(commandName)
          }

          process.on('uncaughtException', (error) => {
            process.emit('error', source, error)
          })

          process.on('unhandledRejection', (error) => {
            process.emit('error', source, error)
          }) // unhandledRejection and uncaughtException emit here so the emitted error can use source.sendFeedback for async

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

              args = args.slice(1)
              if (source.player.trustLevel < command?.data?.trustLevel && !source.sources.console) {
                throw new CommandError({
                  translate: "fnfboyfriendbot.command_manager.validation.invalid_hash",
                  fallback: translations["fnfboyfriendbot.command_manager.validation.invalid_hash"],
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
                  translate: "fnfboyfriendbot.command_manager.validation.invalid_role",
                  fallback: translations["fnfboyfriendbot.command_manager.validation.invalid_role"],
                  color: "red"
                })
            }
          } if (command?.data?.trustLevel === 4 && !source.sources.console)
            throw new CommandError({
              translate: "fnfboyfriendbot.command_manager.trust_level.console_only",
              fallback: translations["fnfboyfriendbot.command_manager.trust_level.console_only"],
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
          process.emit('error', source, error)
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
module.exports = commandManager;
