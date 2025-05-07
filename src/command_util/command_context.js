class CommandContext {
  constructor(name, aliases, description, trustLevel, usages) {
    this.data = {};
    this.data.name = name;
    this.data.aliases = aliases;
    this.data.description = description;
    this.data.trustLevel = trustLevel;
    this.data.usages = usages;
  }
}
module.exports = CommandContext;
