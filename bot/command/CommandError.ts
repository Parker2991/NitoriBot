// ported from FNFBoyfriendBot v8.0.0

const ChatMessage = require("prismarine-chat")("1.21.8");
const stringify = (message: any) => new ChatMessage(message)?.toString();

export class CommandError extends Error {
  public _message: any;

  constructor(message: any) {
    super(stringify(message))
    this.name = "CommandError";
    this._message = message;
  }

  get message() {
    return stringify(this._message);
  }
}
