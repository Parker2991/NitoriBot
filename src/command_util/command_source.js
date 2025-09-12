class CommandSource {
  constructor(player, sources, ChatType) {
    this.player = player;
    this.sources = sources;
    this.ChatType = ChatType;
    this.player.trustLevel = 0;
    this.player.validateBypass = null;
  }
}
module.exports = CommandSource;
