module.exports = {
  data: {
    name: "kill",
    enabled: true,
    aliases: [
    ],
    description: "kills the process",
    usages: [
    ]
  },
  execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    process.exit();
  }
}
