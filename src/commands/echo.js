module.exports = {
  data: {
    name: "echo",
    enabled: true,
    aliases: [
      "say",
      "botsay"
    ],
    description: "send things in chat",
    usages: [
      "<message>"
    ]
  },
  execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    bot.chat(args.join(' '));
  }
}
