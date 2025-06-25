module.exports = {
  data: {
    description: "where the bot is at",
    enabled: true,
    name: "position",
    type: "client"
  },
  inject (context) {
    const bot = context.bot;
    bot.position = null;

    bot.on('packet.position', packet => {
      bot.position = {
        x: packet.x,
        y: packet.y,
        z: packet.z
      };

      bot._client.write('teleport_confirm', { teleportId: packet.teleportId })

      bot.emit('move')
    })

    bot.on('end', () => { bot.position = null })
  }
}
