module.exports = {
  data: {
    name: "customchat",
    enabled: true,
    aliases: [
    ],
    description: "toggle the custom chat",
    usages: [
      "enable/disable"
    ]
  },
  execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    switch (args[0]?.toLowerCase()) {
      case "enable":
        bot.customChat.enabled = true;
        bot.console.command('enabling custom chat');
      break;
      case "disable":
        bot.customChat.enabled = false;
        bot.console.command('disabling custom chat');
      break;
      default:
        bot.console.command(`Custom chat is ${bot.customChat.enabled}`);
    }
  }
}
