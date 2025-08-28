const fs = require("fs");
const path = require("path");
const CommandError = require("../command_util/command_error.js");
const CommandSource = require("../command_util/command_source");
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

    bot.commandManager = {
      commands: {},
      commandlist: [],
      execute(source, commandName, args) {
        const command = this.getCommand(commandName.toLowerCase());
        const authed = bot.auth.list;
        try {
          if (!command) {
            if (source?.sources?.console)
              bot.console.command(
                unknownCommand(commandName, bot),
              );
            else
              source.sendFeedback(
                unknownCommand(commandName, bot),
              );
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
                if (!hasRole)
                  throw new CommandError({
                    text: "You done have the trusted, admin, or owner roles!",
                    color: "red"  
                  });
              } else if (!source?.sources.console) {
                if (source.ChatType === "extras:message") 
                  throw new CommandError({
                    text: "Trusted Commands can not be ran from extras:message",
                    color: "red"
                  })
                if (
                  args.length === 0 &&
                  authed.find((e) => e?.player === source.player.uuid)
                    ?.trustLevel !== "trusted" &&
                    authed.find((e) => e?.player === source.player.uuid)
                      ?.trustLevel !== "admin" &&
                  authed.find((e) => e?.player === source.player.uuid)
                    ?.trustLevel !== "owner"
                )
                  throw new CommandError({
                    text: "Please provide a trusted, admin, or owner hash",
                    color: "red"
                  });
                if (
                  args[0] !== bot.validation.trusted &&
                  args[0] !== bot.validation.admin &&
                  args[0] !== bot.validation.owner &&
                  authed.find((e) => e.player === source.player.uuid)
                    ?.trustLevel !== "trusted" &&
                    authed.find((e) => e.player === source.player.uuid)
                      ?.trustLevel !== "admin" &&
                  authed.find((e) => e.player === source.player.uuid)
                    ?.trustLevel !== "owner"
                )
                  throw new CommandError({
                    text: "Invalid Hash",
                    color: "red"
                  });
                if (source.player.authed) {
                  args = args;
                } else {
                  args = args.slice(1);
                }
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
                if (!hasRole)
                  throw new CommandError({
                    text: "You dont have the admin, or owner roles!",
                    color: "red"
                  });
              } else if (!source?.sources?.console) {
                if (source.ChatType === "extras:message") 
                  throw new CommandError({
                    text: "Admin Commands can not be ran from extras:message",
                    color: "red"
                  })
                if (
                  args.length === 0 &&
                  authed.find((e) => e?.player === source.player.uuid)
                    ?.trustLevel !== "admin" &&
                  authed.find((e) => e?.player === source.player.uuid)
                    ?.trustLevel !== "owner"
                )
                  throw new CommandError({
                    text: "Please provide a hash",
                    color: "red"
                  });
                if (
                  args[0] !== bot.validation.admin &&
                  args[0] !== bot.validation.owner &&
                  authed.find((e) => e?.player === source.player.uuid)
                    ?.trustLevel !== "admin" &&
                  authed.find((e) => e?.player === source.player.uuid)
                    ?.trustLevel !== "owner"
                )
                  throw new CommandError({
                    text: "Invalid hash",
                    color: "red"
                  });

                if (source.player.authed) {
                  args = args;
                } else {
                  args = args.slice(1);
                }
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
                  throw new CommandError({
                    text: "You dont have the owner role!",
                    color: "red"
                  });
              } else if (!source?.sources?.console) {
                if (source.ChatType === "extras:message") 
                  throw new CommandError({
                    text: "Owner Commands can not be ran from extras:message",
                    color: "red"
                  })

                if (
                  args.length === 0 &&
                  bot.validation.owner &&
                  authed.find((e) => e?.player === source.player.uuid)
                    ?.trustLevel !== "owner"
                )
                  throw new CommandError({
                    text: "Please provide a hash",
                    color: "red"
                  });

                if (
                  args[0] !== bot.validation.owner &&
                  authed.find((e) => e?.player === source.player.uuid)
                    ?.trustLevel !== "owner"
                )
                  throw new CommandError({
                    text: "Invalid hash",
                    color: "red"
                  });

                if (source.player.authed) {
                  args = args;
                } else {
                  args = args.slice(1);
                }
              }
              break;
            case 4:
              if (!source?.sources?.console) {
                throw new CommandError({
                  text: "This Command can only be ran via console",
                  color: "red"
                });
              }
              break;
            case 5:
              throw new CommandError({
                text: "This Command has been disabled!",
                color: "red"
              });
              break;
          }

          /*          if (source.sources.discord) {
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
          }*/

          //          if (command.playerOnly === true && source.ChatType.find((e) => e)) return;

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
