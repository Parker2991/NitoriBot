class CommandContext {
  constructor(name, aliases, description, trustLevel, usages, playerOnly) {
    this.data = {};
    this.data.name = name;
    this.data.aliases = aliases;
    this.data.description = description;
    this.data.trustLevel = trustLevel;
    this.data.usages = usages;
    this.data.playerOnly = playerOnly
  }
}
module.exports = CommandContext;
