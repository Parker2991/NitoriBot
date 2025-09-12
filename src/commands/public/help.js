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

    let component = [];
    let infoComponent = [];
    let usagesComponent = [];
    let Public = [];
    let trusted = [];
    let admin = [];
    let owner = [];
    let Console = [];

    let cat = {
      translate: "(%s | %s | %s | %s)",
      color: config.colors.commands.tertiary,
      with: [
        { text: "Public", color: config.colors.help.public },
        { text: "Trusted", color: config.colors.help.trusted },
        { text: "Admin", color: config.colors.help.admin },
        { text: "Owner", color: config.colors.help.owner },
      ],
    };

    component.push({
      translate: "%s: (%s) %s \u203a\n",
      color: config.colors.commands.tertiary,
      with: [
        { text: "Commands", color: config.colors.commands.secondary },
        {
          text: `${bot.commandManager.commandlist.length}`,
          color: config.colors.integer,
        },
        cat,
      ],
    });
    for (const commands of bot.commandManager.commandlist) {
      switch (commands.data.trustLevel) {
        case 0:
          Public.push({
            text: commands.data.name + " ",
            color: config.colors.help.public,
          });
          break;
        case 1:
          trusted.push({
            text: commands.data.name + " ",
            color: config.colors.help.trusted,
          });
          break;
        case 2:
          admin.push({
            text: commands.data.name + " ",
            color: config.colors.help.admin,
          });
          break;
        case 3:
          owner.push({
            text: commands.data.name + " ",
            color: config.colors.help.owner,
          });
          break;
        case 4:
          Console.push({
            text: commands.data.name + " ",
            color: config.colors.help.console,
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
        source.sendFeedback(infoComponent)
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

    source.sendFeedback(component)
  }
}

module.exports = help;
