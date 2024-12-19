module.exports = {
  data: {
    name: "rc",
    enabled: true,
    aliases: [
      "refill",
      "refillcore"
    ],
    description: "refills the core",
    usages: [
    ]
  },
  execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    const config = context.config;
    if (config.core.itemRefill === true) {
      bot.core.itemRefill();
    } else {
      bot.core.chatRefill();
    }
    bot.console.command('refilling core,...');
  }
}
