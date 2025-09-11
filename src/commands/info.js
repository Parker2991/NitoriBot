const { request } = require('undici');
const { execSync } = require('child_process');
const { buildstring } = require('../data/info.json');

module.exports = {
  data: {
    name: "info",
    enabled: true,
    aliases: [
      "information",
    ],
    description: "bot information",
    usages: [
      "version"
    ]
  },
  async execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    const config = context.config;
    let component = [];

    switch (args[0]?.toLowerCase()) {
      case "version":
        let fnfbfbotversionurl = await request('https://code.chipmunk.land/api/v1/repos/Parker2991/FridayNightFunkinBoyfriendBot/releases');
        let fnfbfbotcommiturl = await request('https://code.chipmunk.land/api/v1/repos/Parker2991/FridayNightFunkinBoyfriendBot/commits');

        let urlVersionData = await fnfbfbotversionurl.body.json();
        let urlCommitData = await fnfbfbotcommiturl.body.json();
        let urlVersionOutput = urlVersionData[0];
        let urlCommitOutput = urlCommitData[0];

        component.push({
          translate: "%s%s-%s\n%s: %s\n%s: %s\n\n%s%s%s-%s\n%s: %s",
          color: "dark_gray",
          with: [
            { text: "Ski", color: "aqua" },
            { text: "Bot", color: "blue" },
            { text: `${buildstring.version}`, color: "blue" },
            { text: "Build", color: "dark_blue" },
            { text: `${buildstring.build}`, color: "blue" },
            { text: "Commit", color: "dark_blue" },
            { text: `${execSync("git rev-parse HEAD").toString().substring(0, 10).replaceAll('\n', '')}`, color: "blue" },
            { text: "FNF", color: "dark_blue" },
            { text: "Boyfriend", color: "dark_aqua" },
            { text: "Bot", color: "blue" },
            { text: `${urlVersionOutput.name}`, color: "blue" },
            { text: "Commit", color: "dark_blue" },
            { text: `${urlCommitOutput.sha.substring(0, 10)}`, color: "blue" }
          ]
        });
      break;
      default:
        bot.console.command('SkiBot is a client chat bot created by Parker2991 using Custom FNFBoyfriendBot source\nSkiBot\'s source can be found at https://code.chipmunk.land/Parker2991/SkiBot.git\nFNFBoyfriendBot\'s source can be found at https://code.chipmunk.land/Parker2991/FridayNightFunkinBoyfriendBot.git\n\nthe idea of the bot is to try and make it close to a minecraft java client as much as i can')
    }
    bot.console.command(component);
  }
}
