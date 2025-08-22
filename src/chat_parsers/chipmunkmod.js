function chipmunkmod(message, data, context, bot) {
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
    const hoverEvent = senderComponent.hoverEvent;
    if (hoverEvent?.action === "show_entity") {
      const id = hoverEvent.contents.id;

      sender = data.players.find((player) => player.uuid === id);
    } else {
      const stringUsername = data
        .getMessageAsPrismarine(senderComponent)
        .toString(); // TypeError: data.getMessageAsPrismarine is not a function

      sender = data.players.find(
        (player) => player.profile.name === stringUsername,
      );
    }

    if (!sender) return null;

    return {
      sender,
      contents,
      type: "minecraft:chat",
      chatType: ["system"],
      senderComponent,
    };
  } catch (e) {
    console.error(e);
  }
}
module.exports = chipmunkmod;
