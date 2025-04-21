const fs = require('fs');
const path = require('path');
const CommandError = require('../util/command_error.js');
const CommandSource = require('../util/command_source');
const fixansi = require('../util/ansi');
const cmdMgrUtil = require('../util/command_manager_util');
const sleep = require('../util/sleep.js');

async function inject (context) {
  const bot = context.bot;
  const config = context.config;
  const discordClient = context.discordClient;
  const options = context.options;
  const { MessageBuilder } = require('prismarine-chat')(bot.options.version);
  bot.commandManager = {
    commands: {},
    commandlist: [],
    execute (source, commandName, args) {
      const command = this.getCommand(commandName.toLowerCase());
      try {
        if (!command) {
          if (source?.sources?.console) {
            bot.console.warn(bot.getMessageAsPrismarine({
              translate: "%s%s%s %s",
              color: "dark_gray",
              with: [
                { translate: "command.unknown.command", color: "red" },
                { text: "\n" },
                { text: `${commandName}` },
                { translate: "command.context.here", color: "red" }
              ]
            })?.toAnsi());
          } else {
            throw new CommandError({
              translate: "%s%s%s %s",
              color: "dark_gray",
              with: [
                { translate: "command.unknown.command", color: "red" },
                { text: "\n" },
                { text: `${commandName}` },
                { translate: "command.context.here", color: "red" }
              ]
            });
          }
        }

        cmdMgrUtil(bot, command, args, source, config);

        return command?.execute({ bot, source, arguments: args, config, discordClient });
      } catch (error) {
        if (error instanceof CommandError) {
          bot.tellraw("@a", error._message);
        } else {
          bot.tellraw("@a", {
            translate: "command.failed",
            color: "red",
            hoverEvent: {
              action: "show_text",
              contents: String(error.stack)
            }
          });

          bot?.console?.warn(error.stack);
        }
      }
    },

    executeString (source, command) {
      const [commandName, ...args] = command.split(' ');
      if (source?.sources?.discord) {
        return this.discordExecute(source, commandName, args);
      } else if (!source?.sources?.discord) {
        return this.execute(source, commandName, args);
      }
    },

    discordExecute(source, commandName, args) {
      const command = this.getCommand(commandName.toLowerCase());

      try {
        if (!command) {
          throw new CommandError(`Unknown Command: ${commandName}`);
        }

        cmdMgrUtil(bot, command, args, source, config);

        if (!command?.discordExecute) {
          throw new CommandError(`${command.data.name} command is not supported in discord!`);
        } else if (source.sources.discord) {
          return command?.discordExecute({ bot, source, arguments: args, config, discordClient, fixansi });
        }
      } catch (error) {
        if (error instanceof CommandError) {
          bot?.discord?.message?.reply(`${bot.getMessageAsPrismarine(error._message)}`);
        } else {
          bot?.discord?.message?.reply(`\`\`\`${error.stack}\`\`\``);
          bot?.console?.warn(error.stack);
        }
      }
    },

    register (command) {
      this.commands[command.data.name] = command
      if (command.data.aliases) {
        command.data.aliases.map((a) => (this.commands[a] = command));
      }
    },

    getCommand (name) {
      return this.commands[name]
    },

    getCommands () {
      return Object.values(this.commands)
    },
  }

  /*
   file loader ported from my discord bot SkiBot
   and edited to support mjs files and to support FNFBoyfriendBot's command format
  */
  commandlist = [];
  const foldersPath = path.join(__dirname, '../commands');
  const commandFolders = fs.readdirSync(foldersPath);
  for (const folder of commandFolders) {
    const commandsPath = path.join(foldersPath, folder);
    const commandFiles = fs.readdirSync(commandsPath)
    for (const filename of commandFiles) {
      try {
        const filePath = path.join(commandsPath, filename);
        if (filename.endsWith('.mjs')) {
          let command = await import(filePath);
          bot.commandManager.register(command.default);
          bot.commandManager.commandlist.push(command.default);
        }
        if (filename.endsWith('.js')) {
          let command = require(filePath);
          bot.commandManager.register(command);
          bot.commandManager.commandlist.push(command);
        }
      } catch (error) {
        console.error('Failed to load command ', filename, ':', error);
      }
    }
  }
}

module.exports = {
  data: {
    enabled: true,
    name: "command manager",
    type: "commands"
  },
  inject
};
