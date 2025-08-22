const ChatMessage = require("prismarine-chat")("1.20.2");
const util = require("util");
function parseMessage(messageobj, data) {
  try {
    let match;
    let sender;
    const stringify = (message) => new ChatMessage(message).toString();
    const message = stringify(messageobj);
    const playerWithPrefix = /^(.*?) (\S*?) » (.*?)$/;
    const playerWithoutPrefix = /^(\S*?) » (.*?)$/;
    if (playerWithPrefix.test(message)) {
      match = message.match(playerWithPrefix);

      sender = data.players.find((player) => player.profile.name === match[2]);
      return {
        sender,
        contents: match[3],
        chatType: "player",
        type: "minecraft:chat",
      };
    }
  } catch (e) {
    console.log(e.stack);
  }
}
module.exports = parseMessage;
