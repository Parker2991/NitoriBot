const crypto = require('crypto');
module.exports = {
  data: {
    name: "validate",
    enabled: true,
    aliases: [
      "val"
    ],
    description: "validate through bots",
    usages: [
      "chomens <arguments>",
      "fnfboyfriendbot <arguments>",
      "qbot <arguments>",
      "sbot <arguments>",
      "testbot <arguments>",
    ]
  },
  execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    const config = context.config;
    let hash;
    switch (args[0]) {
      case "chomens":
        const time = Math.floor(Date.now() / 5_000)

        const value = bot.uuid + args.slice(1) + time + config.bots.chomens.key

        hash = crypto.createHash('sha256').update(value).digest('hex').substring(0, 16)
        if (bot.customChat.enabled) {
          bot.customChat.chat(`${config.bots.chomens.prefix}${args.slice(1).shift()} ${hash} ${args.slice(2).join(' ')}`);
        } else {
          bot.chat(`${config.bots.chomens.prefix}${args.slice(1).shift()} ${hash} ${args.slice(2).join(' ')}`);
        }
      break;
      case "fnfboyfriendbot":
        hash = crypto.createHash('sha256').update(Math.floor(Date.now() / 1000) + config.bots.fnfboyfriendbot.key).digest("hex").substring(0, 16);
        if (bot.customChat.enabled) {
          bot.customChat.chat(`${config.bots.fnfboyfriendbot.prefix}${args.slice(1).shift()} ${hash} ${args.slice(2).join(' ')}`)
        } else {
          bot.chat(`${config.bots.fnfboyfriendbot.prefix}${args.slice(1).shift()} ${hash} ${args.slice(2).join(' ')}`);
        }
      break;
      case "qbot":
        hash = crypto.createHash('sha256').update(Math.floor(Date.now() / 1000) + config.bots.qbot.key).digest("hex").substring(0, 16);
        if (bot.customChat.enabled) {
          bot.customChat.chat(`${config.bots.qbot.prefix}${args.slice(1).shift()} ${hash} ${args.slice(2).join(' ')}`);
        } else {
          bot.chat(`${config.bots.qbot.prefix}${args.slice(1).shift()} ${hash} ${args.slice(2).join(' ')}`);
        }
      break;
      case "sbot":

      break;
      case "testbot":

      break;
      default:
        console.warn('unknown argument');
    }
  }
}
