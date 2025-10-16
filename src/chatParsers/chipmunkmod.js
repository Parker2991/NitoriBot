function chipmunkmod(message, data) {
  try {
    if (message === null || typeof message !== "object") return;
    if (
      message.with?.length < 3 ||
      (message.translate !== "[%s] %s › %s" &&
        message.translate !== "%s %s › %s")
    )
      return;

    const senderComponent = message.with[1];
    const contents = message.with[2];
    let sender;
    const hoverEvent = senderComponent.hover_event;
   // console.log(hoverEvent)
    if (hoverEvent?.action === "show_entity") {
      const id = hoverEvent.uuid;

      sender = data.players.find((player) => player.entityUuid[0] === id[0])//.entityUuid //=== hoverEvent.uuid)//.entityUuid;
    } else {
      const stringUsername = data
        .getMessageAsPrismarine(senderComponent)
        ?.toString();

      sender = data.players.find(
        (player) => player.profile.name === stringUsername,
      );
    }

    if (!sender) return null;

    return {
      sender,
      contents,
      type: "minecraft:chat",
      senderComponent,
      chatType: data.chatType
    };
  } catch (e) {
    console.error(e);
  }
}
module.exports = chipmunkmod;
