module.exports = {
  data: {
    name: "core",
    enabled: true,
    aliases: [
      "cb",
      "corerun"
    ]
  },
  execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    bot.core.run(args.join(' '));
  }
}
