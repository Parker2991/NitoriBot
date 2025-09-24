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

      }
    });
  }
}
module.exports = directMessages;