class CommandContext {
  constructor(name, aliases, description, trustLevel, usages, playerChat) {
    this.data = {};
    this.data.name = name;
    this.data.aliases = aliases;
    this.data.description = description;
    this.data.trustLevel = trustLevel;
    this.data.usages = usages;
    this.data.playerChat = playerChat;
  }
}
module.exports = CommandContext;
