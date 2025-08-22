// TODO: Improve how messages are stringified
const ChatMessage = require("prismarine-chat")("1.20.2");
const stringify = (message) => new ChatMessage(message)?.toString();

class CommandError extends Error {
  constructor(message, filename, lineError, useChat) {
    super(stringify(message), filename, lineError, useChat);
    this.name = "CommandError";
    this._message = message;
    return (this._useChat = useChat);
    //    this._useChat = useChat
  }

  get message() {
    return stringify(this._message);
  }
}
module.exports = CommandError;
