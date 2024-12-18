module.exports = {
  data: {
    enabled: true,
    name: "selfcare",
    type: "client"
  },
  inject (context) {
    const bot = context.bot;
    const config = context.config;

    let entityId;
    let permission = 2
    let gameMode;

    bot.on('packet.entity_status', (data) => {
      if (data.entityId !== entityId || data.entityStatus < 24 || data.entityStatus > 28) return;
      permission = data.entityStatus - 24
    });

    bot.on('packet.game_state_change', (data) => {
      if (data.reason !== 3) return; // Reason 3 = Change Game Mode
      if (data.reason !== 4) return;
      gameMode = data.gameMode;
    });

    let timer;
    bot.on('packet.login', (data) => {
      console.log(data.gameMode);
      entityId = data.entityId;
      gameMode = data.gameMode;
      timer = setInterval(() => {
        if (permission < 2) bot.chat("/minecraft:op @s[type=player]");
        else if (gameMode !== 1) bot.chat("/minecraft:gamemode creative");

        else if (gameMode !== 4) bot._client.write("client_command", { actionId: 0 });
      }, 1000);
    });
  }
}
