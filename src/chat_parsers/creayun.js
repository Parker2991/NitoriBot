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
      //    console.log(match[2])
      //    console.log(data.players.find(player => player.profile.name.toString().replaceAll('§','') === match[2]))
      //    console.log(data.players)
      //    console.log(match[0])
      //    console.log(match[2])
      //    console.log(data.players.find(player => player.profile.name === match[2]))
      sender = data.players.find((player) => player.profile.name === match[2]);
      //    sender = data.players.find(player => util.isDeepStrictEqual(player.displayName, playerListDisplayName))
      //    if (!sender) return;
      //    console.log(sender)
      return { sender, contents: match[3], type: "minecraft:chat" };
    }
  } catch (e) {
    console.log(e.stack);
  }
}
module.exports = parseMessage;
