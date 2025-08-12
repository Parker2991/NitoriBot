class position {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    bot.position = null;

    bot.on("packet.position", (packet) => {
      bot.position = {
        x: packet.x,
        y: packet.y,
        z: packet.z,
      };

      bot._client.write("teleport_confirm", { teleportId: packet.teleportId });

      bot.emit("move");
    });

    bot.on("end", () => {
      bot.position = null;
    });
  }
}
module.exports = position;
