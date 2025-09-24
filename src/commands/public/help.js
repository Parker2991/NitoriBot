const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");

class help extends CommandContext {
  constructor() {
    super(
      "help",
      [
        "heko",
        "?",
        "cmds", 
        "commands", 
        "hell"
      ],
      "see a list of commands",
      trustLevel.public,
      ["<command>"],
    );
  }

  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const config = context.config;
    const source = context.source;
    const translations = bot.translations;

    let component = [];
    let infoComponent = [];
    let usagesComponent = [];
    let Public = [];
    let trusted = [];
    let admin = [];
    let owner = [];
    let Console = [];

    component.push({
      translate: "%s %s \u203a\n",
      color: config.colors.commands.tertiary,
      with: [
        {
          translate: "fnfboyfriendbot.command.help.command_layout",
          fallback: translations["fnfboyfriendbot.command.help.command_layout"],
          color: config.colors.commands.primary,
          with: [
            { text: ':', color: config.colors.commands.tertiary },
            { text: '(', color: config.colors.commands.tertiary },
            { text: `${bot.commandManager.commandlist.length}`, color: config.colors.integer },
            { text: ')', color: config.colors.commands.tertiary }
          ]
        },
        {
          translate: "fnfboyfriendbot.command.help.category",
          fallback: translations["fnfboyfriendbot.command.help.category"],
          with: [
            {
              translate: "fnfboyfriendbot.command.help.trust_level.public",
              fallback: translations["fnfboyfriendbot.command.help.trust_level.public"],
              color: config.colors.help.public
            },
            {
              translate: "fnfboyfriendbot.command.help.trust_level.trusted",
              fallback: translations["fnfboyfriendbot.command.help.trust_level.trusted"],
              color: config.colors.help.trusted
            },
            {
              translate: "fnfboyfriendbot.command.help.trust_level.admin",
              fallback: translations["fnfboyfriendbot.command.help.trust_level.admin"],
              color: config.colors.help.admin
            },
            {
              translate: "fnfboyfriendbot.command.help.trust_level.owner",
              fallback: translations["fnfboyfriendbot.command.help.trust_level.owner"],
              color: config.colors.help.owner
            }
          ]
        }
      ]
    })


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
          translate: "%s\n%s\n%s\n%s\n%s",
          color: config.colors.commands.tertiary,
          with: [
            {
              translate: "fnfboyfriendbot.command.help.info.command",
              fallback: translations["fnfboyfriendbot.command.help.info.command"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                commands.data.name,
              ]
            },
            {
              translate: "fnfboyfriendbot.command.help.info.alias",
              fallback: translations["fnfboyfriendbot.command.help.info.alias"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                commands.data.aliases.toString().replaceAll(',',' ')
              ]
            },
            {
              translate: "fnfboyfriendbot.command.help.info.description",
              fallback: translations["fnfboyfriendbot.command.help.info.description"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                commands.data.description
              ]
            },
            {
              translate: "fnfboyfriendbot.command.help.info.trust_level",
              fallback: translations["fnfboyfriendbot.command.help.info.trust_level"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${commands.data.trustLevel}`, color: config.colors.integer }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.help.usages",
              fallback: translations["fnfboyfriendbot.command.help.info.usages"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                usagesComponent
              ]
            },

          ]
        })
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
