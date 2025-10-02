class CommandContext {
  constructor(name, aliases, description, trustLevel, usages, chatTypes) {
    this.data = {};
    this.data.name = name;
    this.data.aliases = aliases;
    this.data.description = description;
    this.data.trustLevel = trustLevel;
    this.data.usages = usages;
    this.data.chatTypes = chatTypes;
  }
}
module.exports = CommandContext;
