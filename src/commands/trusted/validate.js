const { EmbedBuilder } = require('discord.js');
module.exports = {
  data: {
    name: 'validate',
    trustLevel: 1,
    aliases: [
      "val"
    ],
    description: 'validate through the bot',
    usages: [
      ""
    ],
  },
  execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    const source = context.source;
    if (args[0] === bot.validation.trusted) {
      bot.chat.message('&2Valid Trusted hash');
    }
    if (args[0] === bot.validation.admin) {
      bot.chat.message('&2Valid Admin hash');
    }
    if (args[0] === bot.validation.owner) {
      bot.chat.message('&2Valid Owner hash');
    }
  },
  discordExecute (context) {
    const bot = context.bot;
    const event = bot?.discord?.message
    const roles = event?.member?.roles?.cache
    const source = context.source;
    const config = context.config;

    if (roles?.some(role => role.name === `${config.discord.roles.trusted}`)) {
      bot.discord.message.reply('Valid trusted user')
      bot.chat.message(`Valid trusted user [${bot.discord.message.member.user.username}]`)
    } else if (roles?.some(role => role.name === `${config.discord.roles.owner}` || role.name === `${config.discord.roles.fullAccess}`)) {
      bot.discord.message.reply('Valid Owner user')
      bot.chat.message(`Valid Owner User [${bot.discord.message.member.user.username}]`);
    }
  }
}
