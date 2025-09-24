class gamemode {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    const options = context.options;
    bot.selfcare.gameMode = null;

    bot.on("packet.game_state_change", (packet) => {
      if (packet.reason === 3 || packet.reason === "change_game_mode") {
        bot.selfcare.gameMode = packet.gameMode;
      } if (packet.reason === 4 || packet.reason === "win_game") {
        bot._client.write('client_command', { action: 0 })        
      }
    });
  }
}

module.exports = gamemode;
