function me (message, data) {
  try {
  if (message === null || typeof message !== "object") return;

  if (message.with?.length > 2 || message.translate !== "chat.type.announcement") return;

  const component = message.with[0];
  const contents = message.with[1];
  let sender;
  const hoverEvent = component.hover_event
  
  if (hoverEvent?.action === "show_entity") {
    const name = hoverEvent.name;
    sender = data.players.find((player) => player.profile.name === name)
  }

  if (!sender) return;

  return {
    sender,
    contents,
    type: "minecraft:chat",
    component,
    chatType: data.chatType
  };
  } catch (e) {
    console.log(e.stack)
  }
}

module.exports = me;