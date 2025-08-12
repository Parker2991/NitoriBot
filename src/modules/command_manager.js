const fs = require("fs");
const path = require("path");
const CommandError = require("../command_util/command_error.js");
const CommandSource = require("../command_util/command_source");
const fixansi = require("../util/ansi");
const sleep = require("../util/sleep.js");

function unknownCommand (MessageBuilder, commandName, bot) {
  return new MessageBuilder()
    .setTranslate("%s%s%s %s")
    .setColor("red")
    .addWith(
      new MessageBuilder()
        .setTranslate("command.unknown.command")
          .setColor('red'),
      new MessageBuilder()
        .setText('\n'),
      new MessageBuilder()
        .setText(`${commandName}`)
          .setColor("gray"),
      new MessageBuilder()
        .setTranslate("command.context.here")
        .setColor("red")
    )
}

class command_manager {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    const discordClient = context.discordClient;
    const options = context.options;
    const { MessageBuilder } = require('prismarine-chat')(bot._client.version)


    bot.commandManager = {
      commands: {},
      commandlist: [],
      execute(source, commandName, args) {
        const command = this.getCommand(commandName.toLowerCase());

        try {
          if (!command) {
            if (source?.sources?.console) bot.console.command(
              unknownCommand(MessageBuilder, commandName, bot)
            )
            else source.sendFeedback(unknownCommand(MessageBuilder, commandName, bot))
          }

          const event = bot.discord.message;
          const roles = event?.member?.roles?.cache;

          switch (command?.data?.trustLevel) {
            case 0:
              // do nothing since trust level 0 is public
              break;
            case 1:
              if (source?.sources?.discord) {
                const hasRole = roles?.some(
                  (role) =>
                    role.name === `${config.discord.roles.trusted}` ||
                    role.name === `${config.discord.roles.admin}` ||
                    role.name === `${config.discord.roles.fullAccess}` ||
                    role.name === `${config.discord.roles.owner}`,
                );
                if (!hasRole) throw new CommandError(
                  new MessageBuilder()
                    .setText("You dont have the trusted, admin, or owner roles!")
                    .setColor("red")
                )
              } else if (!source?.sources.console) {
                if (args.length === 0) throw new CommandError(
                  new MessageBuilder()
                    .setText("Please provide a trusted, admin, or owner hash")
                    .setColor("red")
                )
                if (
                  args[0] !== bot.validation.trusted &&
                  args[0] !== bot.validation.admin &&
                  args[0] !== bot.validation.owner
                ) throw new CommandError(
                  new MessageBuilder()
                    .setText("Invalid Hash")
                    .setColor("red")
                )
              }
              break;
            case 2:
              if (source?.sources?.discord) {
                const hasRole = roles?.some(
                  (role) =>
                    role.name === `${config.discord.roles.admin}` ||
                    role.name === `${config.discord.roles.fullAccess}` ||
                    role.name === `${config.discord.roles.owner}`,
                );
                if (!hasRole) throw new CommandError(
                  new MessageBuilder()
                    .setText("You dont have the admin, or owner roles!")
                    .setColor("red")
                )
              } else if (!source?.sources?.console) {
                if (args.length === 0)
                  throw new CommandError(
                    new MessageBuilder()
                      .setText('Please provide a hash')
                      .setColor('red')
                  );
                if (
                  args[0] !== bot.validation.admin &&
                  args[0] !== bot.validation.owner
                )
                  throw new CommandError(
                    new MessageBuilder()
                      .setText('Invalid Hash')
                  );
              }
              break;
            case 3:
              if (source?.sources?.discord) {
                const hasRole = roles?.some(
                  (role) =>
                    role.name === `${config.discord.roles.owner}` ||
                    role.name === `${config.discord.roles.fullAccess}`,
                );
                if (!hasRole)
                  throw new CommandError(
                  new MessageBuilder()
                    .setText("You dont have the owner role!")
                    .setColor("red")
                )
              } else if (!source?.sources?.console) {
                if (args.length === 0 && bot.validation.owner)
                  throw new CommandError(
                    new MessageBuilder()
                      .setText("Please provide a hash")
                      .setColor('red')
                  );

                if (args[0] !== bot.validation.owner)
                  throw new CommandError(
                    new MessageBuilder()
                      .setText('Invalid Hash')
                      .setColor('red')
                  );
              }
              break;
            case 4:
              if (!source?.sources?.console) {

                throw new CommandError(
                  new MessageBuilder()
                    .setText("This Command can only be ran via console")
                    .setColor('red')
                );
              }
              break;
            case 5:
              throw new CommandError(
                new MessageBuilder()
                  .setText("This Command has been disabled!")
                  .setColor('red')
              )
            break
          }

          if (source.sources.discord) {
            discordClient.channels.cache
              .get("1361739286113685534")
              .send(
                `User: ${source.player.profile.name}, Command: ${command?.data?.name}, Args: ${args.join(" ")}, Time/Date: ${new Date().toLocaleString("en-us", { TimeZone: "America/Chicago" })}, Source: Discord`,
              );
          } else if (!source.sources.console) {
            discordClient.channels.cache
              .get("1361739286113685534")
              .send(
                `User: ${source.player.profile.name}, Command: ${command?.data?.name}, Args: ${args.join(" ")}, Time/Date: ${new Date().toLocaleString("en-us", { TimeZone: "America/Chicago" })}, Source: Minecraft, Server: ${bot.options.host}:${bot.options.port}`,
              );
          }

          return command?.execute({
            bot,
            source,
            arguments: args,
            config,
            discordClient,
          });
        } catch (error) {
          if (error instanceof CommandError) {
            if (source.sources.discord) {
              bot.discord.message.reply(
                `\`\`\`ansi\n${new fixansi(bot.getMessageAsPrismarine(error._message)?.toAnsi()).ansi}\`\`\``,
              );
            } else if (source.sources.console) {
              bot.console.warn(error._message);
            } else {
              bot.tellraw("@a", error._message);
            }
          } else {
            if (source.sources.discord) {
              bot.discord.message.reply(
                `\`\`\`ansi\n${new fixansi(bot.getMessageAsPrismarine({ text: error.stack, color: "red"})?.toAnsi()).ansi}\`\`\``,
              );
            } else {
              bot.tellraw("@a", {
                translate: "command.failed",
                color: "red",
                hoverEvent: {
                  action: "show_text",
                  contents: String(error.stack),
                },
              });

              bot?.console?.warn(error.stack);
            }
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
