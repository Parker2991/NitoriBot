function parseMessage(message, data, context) {
  try {
    if (message === null || typeof message !== "object") return;

    if (
      message.with?.length < 2 ||
      (message.translate !== "chat.type.text" && message.translate !== "%s %s")
    )
      return;
    const senderComponent = message.with[0];
    const contents = message.with[1];
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
    if (!sender) return; //stringUsername
    return {
      sender,
      contents,
      type: "minecraft:chat",
      senderComponent,
      chatType: data.chatType
    };
  } catch (e) {
    console.error(`${e.stack}`);
  }
}
module.exports = parseMessage;
