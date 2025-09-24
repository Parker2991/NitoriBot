class op {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    const options = context.options;
    bot.selfcare.entityId = undefined;
    bot.on("packet.entity_status", (packet) => {
      if (
        packet.entityId !== bot.selfcare.entityId ||
        packet.entityStatus < 24 ||
        packet.entityStatus > 28
      )
        return;
      bot.selfcare.permissionLevel = packet.entityStatus - 24;
    });
  }
}

module.exports = op;