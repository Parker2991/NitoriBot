// TODO: Improve how messages are stringified
const ChatMessage = require("prismarine-chat")("1.21.8");
const stringify = (message) => new ChatMessage(message)?.toString();

class CommandError extends Error {
  constructor(message, filename, lineError) {
    super(stringify(message), filename, lineError);
    this.name = "CommandError";
    this._message = message;
  }

  get message() {
    return stringify(this._message);
  }
}
module.exports = CommandError;
