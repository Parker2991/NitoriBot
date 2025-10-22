class CommandContext {
  constructor(name, aliases, description, trustLevel, usages, playerChatOnly) {
    this.data = {};
    this.data.name = name;
    this.data.aliases = aliases;
    this.data.description = description;
    this.data.trustLevel = trustLevel;
    this.data.usages = usages;
    this.data.playerChatOnly = playerChatOnly
  }
}
module.exports = CommandContext;
