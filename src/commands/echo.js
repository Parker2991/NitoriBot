module.exports = {
  data: {
    name: "echo",
    enabled: true,
    aliases: [
      "say",
      "botsay"
    ]
  },
  execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    bot.chat(args.join(' '));
  }
}
