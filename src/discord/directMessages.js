const crypto = require('crypto');

class directMessages {
  constructor (context) {
    const bot = context.bot;
    const config = context.config;
    const discordClient = context.discordClient;

    discordClient.on("messageCreate", async (message) => {
      if (message.guildId !== null) return;
      else {
        const server = discordClient.guilds.cache.get(`${config.discord.serverID}`)
        const member = await server.members.fetch(message.author.id);
        const roles = member.roles.cache;
        let trustLevel = 0;
        let hash;
        const hasRole = roles.some((role) => {
          if (role.name === `${config.discord.roles.trusted}`) trustLevel = 1;
          else if (role.name === `${config.discord.roles.admin}`) trustLevel = 2;
          else if (role.name === `${config.discord.roles.owner}`) trustLevel = 3;
        })

        if (message.content === "hash") {
          if (trustLevel === 1) bot.validation.discord.trusted = crypto.createHash("sha512").digest("hex").substring(0, 16)
          else if (trustLevel === 2) bot.validation.discord.admin = crypto.createHash("sha512").digest("hex").substring(0, 16)
          else if (trustLevel === 3) bot.validation.discord.owner = crypto.createHash("sha512").digest("hex").substring(0, 16)

          if (trustLevel === 1) hash = bot.validation.discord.trusted;
          else if (trustLevel === 2) hash = bot.validation.discord.admin;
          else if (trustLevel === 3) hash = bot.validation.discord.owner
          message.reply(`The hash for trust level ${trustLevel} is ${hash}`)
        } else if (message.content === "key") {
          
        }
      }
    });
  }
}
module.exports = directMessages;