export default function permission (bot: any, config: any) {
  bot.on("packet.entity_status", (packet: any) => {
    if (
      packet.entityId !== bot.entityId
      ||
      packet.entityStatus < 24
      ||
      packet.entityStatus > 28 
    ) return
    bot.selfcare.permission = packet.entityStatus - 24;
  })
}