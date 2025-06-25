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

    if (!args && !args[0] && !args[1] && !args[2] && !args[3]) return;

    switch (args[0]) {
      case "chomens":
        const time = Math.floor(Date.now() / 5_000)

        const value = bot.uuid + args[1] + time + config.bots.chomens.key

        hash = crypto.createHash('sha256').update(value).digest('hex').substring(0, 16)
        if (bot.customChat.enabled) {
          bot.customChat.chat(`${config.bots.chomens.prefix}${args.slice(1).shift()} ${hash} ${args.slice(2).join(' ')}`);
        } else {
          bot.chat(`${config.bots.chomens.prefix}${args.slice(1).shift()} ${hash} ${args.slice(2).join(' ')}`);
        }
      break;
      case "fnfboyfriendbot":
        hash = crypto.createHash('sha256').update(Math.floor(Date.now() / 2000) + config.bots.fnfboyfriendbot.key).digest("hex").substring(0, 16);
        if (bot.customChat.enabled) {
          bot.customChat.chat(`${config.bots.fnfboyfriendbot.prefix}${args.slice(1).shift()} ${crypto.createHash('sha256').update(Math.floor(Date.now() / 2000) + config.bots.fnfboyfriendbot.key).digest('hex').substring(0, 16)} ${args.slice(2).join(' ')}`)
        } else {
          bot.chat(`${config.bots.fnfboyfriendbot.prefix}${args.slice(1).shift()} ${crypto.createHash('sha256').update(Math.floor(Date.now() / 2000) + config.bots.fnfboyfriendbot.key).digest('hex').substring(0, 16)} ${args.slice(2).join(' ')}`);
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
        const md = crypto.createHash('MD5');
        const command = args.slice(1).join(' ');
        const timee = Math.floor(+new Date() / 20000);
        const raw = config.bots.sbot.prefix+`${command.replace(/&[0-9a-fklmnor]/g, '')};${bot.server.username};${timee};${config.bots.sbot.key}`;
        md.update(raw);
        hash = md.digest();
        const big_int = hash.slice(0, 4).readUInt32BE();
        bot.chat(`${config.bots.sbot.prefix}${command} ${big_int.toString(36)}`);

      break;
      case "testbot":
        context.webhook.send(`${bot.server.username}`);
        if (bot.customChat.enabled) {
          bot.customChat.chat(`${config.bots.testbot.prefix}${args.slice(1).join(' ')}`);
        } else {
          bot.chat(`${config.bots.testbot.prefix}${args.slice(1).join(' ')}`);
        }
      break;
      default:
        console.warn('unknown argument');
    }
  }
}
