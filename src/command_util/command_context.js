class CommandContext {
  constructor(name, aliases, description, trustLevel, usages, playerChat, args) {
    this.data = {};
    this.data.name = name;
    this.data.aliases = aliases;
    this.data.description = description;
    this.data.trustLevel = trustLevel;
    this.data.usages = usages;
    this.data.playerChat = playerChat;
    this.data.args = args
  }
}
module.exports = CommandContext;
