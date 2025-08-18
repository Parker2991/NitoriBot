const fixansi = require("../../util/ansi");
const CommandContext = require("../../command_util/command_context");
const CommandTrustLevel = require('../../command_util/command_trust_level')
const trustLevel = new CommandTrustLevel()

class help extends CommandContext {
  constructor() {
    super(
      "help",
      ["heko", "?", "cmds", "commands", "hell"],
      "see a list of commands",
      trustLevel.public,
      ["<command>"],
      false
    );
  }

  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const config = context.config;
    const source = context.source;
    const { MessageBuilder } = require('prismarine-chat')('1.20.2');

    let component = [];
    let infoComponent = [];
    let usagesComponent = [];
    let Public = [];
    let trusted = [];
    let admin = [];
    let owner = [];
    let Console = [];
    let Commands = [];

    function cat () {
      return new MessageBuilder()
        .setTranslate("(%s | %s | %s | %s)")
        .setColor(config.colors.commands.tertiary)
        .addWith(
          new MessageBuilder()
            .setText("Public")
            .setColor(config.colors.help.public),
          new MessageBuilder()
            .setText("Trusted")
            .setColor(config.colors.help.trusted),
          new MessageBuilder()
            .setText("Admin")
            .setColor(config.colors.help.admin),
          new MessageBuilder()
            .setText("Owner")
            .setColor(config.colors.help.owner)
        )
    }

    function command_count () {
      return new MessageBuilder()
        .setTranslate("%s: (%s) %s \u203a")
        .setColor(config.colors.commands.tertiary)
        .addWith(
          new MessageBuilder()
            .setText("Commands")
            .setColor(config.colors.commands.secondary),
          new MessageBuilder()
            .setText(`${bot.commandManager.commandlist.length}`)
            .setColor(config.colors.integer),
          cat()
        )
    }

//    component.push(command_count() + '\n');

    for (const commands of bot.commandManager.commandlist) {
      switch (commands.data.trustLevel) {
        case 0:
          Public.push(
            new MessageBuilder()
              .setText('')
              .addExtra(
                new MessageBuilder()
                  .setText(commands.data.name + ' ')
                  .setColor(config.colors.help.public),
              )
          )
        break
        case 1:
          trusted.push(
            new MessageBuilder()
              .setText('')
              .addExtra(
                new MessageBuilder()
                  .setText(commands.data.name + ' ')
                  .setColor(config.colors.help.trusted),
              )
          )
        break
        case 2:
          admin.push(
            new MessageBuilder()
              .setText('')
              .addExtra(
                new MessageBuilder()
                  .setText(commands.data.name + ' ')
                  .setColor(config.colors.help.admin),
              )
          )
        break
        case 3:
          owner.push(
            new MessageBuilder()
              .setText('')
              .addExtra(
                new MessageBuilder()
                  .setText(commands.data.name + ' ')
                  .setColor(config.colors.help.owner),
              )
          )
        break
        case 4:
          Console.push(
            new MessageBuilder()
              .setText('')
              .addExtra(
                new MessageBuilder()
                  .setText(commands.data.name + ' ')
                  .setColor(config.colors.help.console),
              )
          )
        break
      }

      if (
        args[0]?.toLowerCase() === commands.data.name?.toLowerCase() ||
        commands.data.aliases.find(
          (e) => e?.toLowerCase() === args[0]?.toLowerCase(),
        )
      ) {
        
/*        for (const usages of commands.data.usages) {

        }*/
      }
    }
    if (Public.length > 0) Commands.push(Public, '\n')
    if (trusted.length > 0) Commands.push(trusted, '\n')
    if (admin.length > 0) Commands.push(admin, '\n')
    if (owner.length > 0) Commands.push(owner)
    if (source.sources.console && Console.length > 0) Commands.push('\n', Console)

    component.push([command_count(), '\n', Commands])
    bot.tellraw("@a", component)
/*

    for (const commands of bot.commandManager.commandlist) {
      switch (commands.data.trustLevel) {
        case 0:
          Public.push({
            text: commands.data.name + " ",
            color: config.colors.help.public,
            translate: "",
            hoverEvent: {
              action: "show_text",
              value: [
                {
                  translate: "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
                  color: config.colors.commands.tertiary,
                  with: [
                    {
                      text: "Command Name",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.name}`,
                      color: config.colors.commands.secondary,
                    },
                    {
                      text: "Trust Level",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.trustLevel}`,
                      color: config.colors.integer,
                    },
                    {
                      text: "Description",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.description}`,
                      color: config.colors.commands.secondary,
                    },
                    { text: "Aliases", color: config.colors.commands.primary },
                    {
                      text: `${commands.data.aliases}`,
                      color: config.colors.commands.secondary,
                    },
                    { text: "Usages", color: config.colors.commands.primary },
                    {
                      text: `${commands.data.usages}`,
                      color: config.colors.commands.secondary,
                    },
                  ],
                },
              ],
            },
            clickEvent: {
              action: "suggest_command",
              value: `${config.prefixes[0]}${commands.data.name}`,
            },
          });
          break;
        case 1:
          trusted.push({
            text: commands.data.name + " ",
            color: config.colors.help.trusted,
            translate: "",
            hoverEvent: {
              action: "show_text",
              value: [
                {
                  translate: "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
                  color: config.colors.commands.tertiary,
                  with: [
                    {
                      text: "Command Name",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.name}`,
                      color: config.colors.commands.secondary,
                    },
                    {
                      text: "Trust Level",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.trustLevel}`,
                      color: config.colors.integer,
                    },
                    {
                      text: "Description",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.description}`,
                      color: config.colors.commands.secondary,
                    },
                    { text: "Aliases", color: config.colors.commands.primary },
                    {
                      text: `${commands.data.aliases}`,
                      color: config.colors.commands.secondary,
                    },
                    { text: "Usages", color: config.colors.commands.primary },
                    {
                      text: `${commands.data.usages}`,
                      color: config.colors.commands.secondary,
                    },
                  ],
                },
              ],
            },
            clickEvent: {
              action: "suggest_command",
              value: `${config.prefixes[0]}${commands.data.name}`,
            },
          });
          break;
        case 2:
          admin.push({
            text: commands.data.name + " ",
            color: config.colors.help.admin,
            translate: "",
            hoverEvent: {
              action: "show_text",
              value: [
                {
                  translate: "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
                  color: config.colors.commands.tertiary,
                  with: [
                    {
                      text: "Command Name",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.name}`,
                      color: config.colors.commands.secondary,
                    },
                    {
                      text: "Trust Level",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.trustLevel}`,
                      color: config.colors.integer,
                    },
                    {
                      text: "Description",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.description}`,
                      color: config.colors.commands.secondary,
                    },
                    { text: "Aliases", color: config.colors.commands.primary },
                    {
                      text: `${commands.data.aliases}`,
                      color: config.colors.commands.secondary,
                    },
                    { text: "Usages", color: config.colors.commands.primary },
                    {
                      text: `${commands.data.usages}`,
                      color: config.colors.commands.secondary,
                    },
                  ],
                },
              ],
            },
            clickEvent: {
              action: "suggest_command",
              value: `${config.prefixes[0]}${commands.data.name}`,
            },
          });
          break;
        case 3:
          owner.push({
            text: commands.data.name + " ",
            color: config.colors.help.owner,
            translate: "",
            hoverEvent: {
              action: "show_text",
              value: [
                {
                  translate: "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
                  color: config.colors.commands.tertiary,
                  with: [
                    {
                      text: "Command Name",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.name}`,
                      color: config.colors.commands.secondary,
                    },
                    {
                      text: "Trust Level",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.trustLevel}`,
                      color: config.colors.integer,
                    },
                    {
                      text: "Description",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.description}`,
                      color: config.colors.commands.secondary,
                    },
                    { text: "Aliases", color: config.colors.commands.primary },
                    {
                      text: `${commands.data.aliases}`,
                      color: config.colors.commands.secondary,
                    },
                    { text: "Usages", color: config.colors.commands.primary },
                    {
                      text: `${commands.data.usages}`,
                      color: config.colors.commands.secondary,
                    },
                  ],
                },
              ],
            },
            clickEvent: {
              action: "suggest_command",
              value: `${config.prefixes[0]}${commands.data.name}`,
            },
          });
          break;
        case 4:
          Console.push({
            text: commands.data.name + " ",
            color: config.colors.help.console,
            translate: "",
            hoverEvent: {
              action: "show_text",
              value: [
                {
                  translate: "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
                  color: config.colors.commands.tertiary,
                  with: [
                    {
                      text: "Command Name",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.name}`,
                      color: config.colors.commands.secondary,
                    },
                    {
                      text: "Trust Level",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.trustLevel}`,
                      color: config.colors.integer,
                    },
                    {
                      text: "Description",
                      color: config.colors.commands.primary,
                    },
                    {
                      text: `${commands.data.description}`,
                      color: config.colors.commands.secondary,
                    },
                    { text: "Aliases", color: config.colors.commands.primary },
                    {
                      text: `${commands.data.aliases}`,
                      color: config.colors.commands.secondary,
                    },
                    { text: "Usages", color: config.colors.commands.primary },
                    {
                      text: `${commands.data.usages}`,
                      color: config.colors.commands.secondary,
                    },
                  ],
                },
              ],
            },
            clickEvent: {
              action: "suggest_command",
              value: `${config.prefixes[0]}${commands.data.name}`,
            },
          });
          break;
        default:
      }

      if (
        args[0]?.toLowerCase() === commands.data.name?.toLowerCase() ||
        commands.data.aliases.find(
          (e) => e?.toLowerCase() === args[0]?.toLowerCase(),
        )
      ) {
        for (const usages of commands.data.usages) {
          usagesComponent.push({
            translate: "%s%s %s",
            with: [
              {
                text: `${config.prefixes[0]}`,
                color: config.colors.commands.primary,
              },
              {
                text: `${commands.data.name}`,
                color: config.colors.commands.secondary,
              },
              { text: `${usages}`, color: config.colors.commands.secondary },
            ],
          });

          usagesComponent.push("\n");
        }

        usagesComponent.pop();

        infoComponent.push({
          translate: "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s:\n",
          color: config.colors.commands.tertiary,
          with: [
            { text: "Name", color: config.colors.commands.primary },
            {
              text: `${commands.data.name}`,
              color: config.colors.commands.secondary,
            },
            { text: "Aliases", color: config.colors.commands.primary },
            {
              text: `${commands.data.aliases.toString().replaceAll(",", " ")}`,
              color: config.colors.commands.secondary,
            },
            { text: "Description", color: config.colors.commands.primary },
            {
              text: `${commands.data.description}`,
              color: config.colors.commands.secondary,
            },
            { text: "Trust Level", color: config.colors.commands.primary },
            {
              text: `${commands.data.trustLevel}`,
              color: config.colors.integer,
            },
            { text: "Usages", color: config.colors.commands.primary },
          ],
        });

        infoComponent.push(usagesComponent);

        if (source?.sources.console) {
          bot.console.info(bot.getMessageAsPrismarine(infoComponent)?.toAnsi());
        } else if (source.sources.discord) {
          bot.discord.message.reply(
            `\`\`\`ansi\n${fixansi(bot.getMessageAsPrismarine(infoComponent)?.toAnsi())}\`\`\``,
          );
        } else {
          bot.tellraw(`@a`, infoComponent);
        }
        return;
      }
    }

    if (Public.length > 0) {
      component.push(Public);
      if (trusted.length > 0) component.push("\n");
    }

    if (trusted.length > 0) {
      component.push(trusted);
      if (admin.length > 0) component.push("\n");
    }

    if (admin.length > 0) {
      component.push(admin);
      if (owner.length > 0) component.push("\n");
    }

    if (owner.length > 0) {
      component.push(owner);
    }

    if (source.sources.console && Console.length > 0) {
      component.push(Console);
    }

    if (source?.sources?.console) {
      bot.console.info(bot.getMessageAsPrismarine(component)?.toAnsi());
    } else if (source.sources.discord) {
      bot.discord.message.reply(
        `\`\`\`ansi\n${fixansi(bot.getMessageAsPrismarine(component)?.toAnsi())}\`\`\``,
      );
    } else {
      bot.tellraw("@a", component);
    }*/
  }
}

module.exports = help;
