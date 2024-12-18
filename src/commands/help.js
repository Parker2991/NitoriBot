module.exports = {
  data: {
    name: "help",
    enabled: true,
    aliases: [
      "heko",
      "?",
      "cmds",
      "commands"
    ],
    description: "get a list of the commands",
    usages: [
      "<command>"
    ]
  },
  execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    const config = context.config;
    let commandComponent = [];
    let outputComponent = [];
    let colorMix = true;
    for (const commands of bot.commandManager.commandlist) {
      let usagesComponent = [];
      let infoComponent = [];
      for (const usages of commands.data.usages) {
        usagesComponent.push({
          translate: "%s%s %s",
          with: [
            { text: `${config.console.prefix}`, color: "dark_blue" },
            { text: `${commands.data.name}`, color: "aqua" },
            { text: `${usages}`, color: "blue" },
          ]
        });

        usagesComponent.push("\n");
      }
      usagesComponent.pop();

      infoComponent.push({
        translate: "%s: %s\n%s: %s\n%s: %s\n%s:",
        color: "dark_gray",
        with: [
          { text: "Command Name", color: "dark_blue" },
          { text: `${commands.data.name}`, color: "blue" },
          { text: "Aliases", color: "dark_blue" },
          { text: `${commands.data.aliases.toString().replaceAll(',',' ')}`, color: "blue" },
          { text: "Description", color: "dark_blue" },
          { text: `${commands.data.description}`, color: "blue" },
          { text: "Usages", color: "dark_blue" },
        ]
      });

      infoComponent.push("\n");

      infoComponent.push(usagesComponent);

      if (args[0]?.toLowerCase() === commands.data.name) {
        bot.console.command(infoComponent);
        return;
      }

      commandComponent.push({
        text: commands.data.name + ' ',
        color: (!(colorMix = !colorMix) ? "aqua" : "dark_blue")
      });
    }

    outputComponent.push({
      translate: "%s (%s) \u203a\n %s",
      color: "dark_gray",
      with: [
        { text: "Commands", color: "blue" },
        { text: `${bot.commandManager.commandlist.length}`, color: "gold" },
        commandComponent
      ]
    })

    bot.console.command(outputComponent);
  }
}
