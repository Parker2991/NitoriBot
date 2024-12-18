module.exports = {
  data: {
    name: "core",
    enabled: true,
    aliases: [
      "cb",
      "corerun"
    ],
    description: "run commands in core",
    usages: [
      "<command>"
    ]
  },
  execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    bot.core.run(args.join(' '));
  }
}
