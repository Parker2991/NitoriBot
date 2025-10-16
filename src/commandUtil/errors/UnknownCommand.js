class UnknownCommand extends Error {
  constructor(commandName) {
    super(commandName);
    this.name = "UnknownCommand";
    this._message = {
      translate: "%s%s%s %s",
      color: "red",
      with: [
        { translate: "command.unknown.command", color: "red" },
        { text: "\n"},
        { text: `${commandName}`, color: "gray" },
        { translate: "command.context.here", color: "red" }
      ]
    }
  }
}
module.exports = UnknownCommand;
