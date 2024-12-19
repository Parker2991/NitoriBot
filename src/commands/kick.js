module.exports = {
  data: {
    name: "kick",
    enabled: true,
    aliases: [
    ],
    description: "kick players",
    usages: [
      "invalidstring <player>",
      "item <player>",
    ]
  },
  execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    switch (args[0]?.toLowerCase()) {
      case "invalidstring":
        bot.exploits.crashes.invalidString(args.slice(1).join(' '));
        bot.console.command(`Crashing ${args.slice(1).join(' ')}`);
      break;
      case "item":
        bot.exploits.kicks.item(args.slice(1).join(' '));
        bot.console.command(`Kicking ${args.slice(1).join(' ')}`);
      break;
      default:
        bot.console.command("invalid argument");
    }
  }
}
