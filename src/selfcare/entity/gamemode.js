class gamemodeSelfcare {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    const options = context.options;
    bot.selfcare.gameMode = null;

    bot.on("packet.game_state_change", (packet) => {
      if (packet.reason !== 3) return; // Reason 3 = Change Game Mode
      bot.selfcare.gameMode = packet.gameMode;
    });
  }
}

module.exports = gamemodeSelfcare;
