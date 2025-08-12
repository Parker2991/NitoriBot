class CommandSource {
  constructor(player, sources, hash) {
    this.player = player;
    this.sources = sources;
    this.player.hash = hash;
  }
}
module.exports = CommandSource;
