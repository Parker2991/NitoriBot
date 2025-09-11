const { fromNotch } = require('prismarine-chat')('1.20.2');
module.exports = {
  data: {
    description: "self explanatory",
    enabled: true,
    name: "selfcare",
    type: "client"
  },
  inject (context) {
    const bot = context.bot;
    const config = context.config;

    let entityId;
    let permission = 2;
    let gameMode;
    let positionCount = 0;
    let mute = false;
    let god = false;
    let commandSpy = false;
    let prefix = false;
    let vanish = false;
    let nick = false;
    let username = false;
    
    bot.on("system_chat_selfcare", (message) => {
      try {
      const parsedMessage = fromNotch(message)?.toMotd();
      if (parsedMessage?.startsWith("§6You have been muted")) mute = true;
      else if (parsedMessage?.startsWith("§6You have been unmuted")) mute = false;
      else if (parsedMessage?.startsWith("§6Your voice has been silenced")) mute = true;

      else if (parsedMessage === "§6God mode§c enabled§6.") god = true;
      else if (parsedMessage === "§6God mode§c disabled§6.") god = false;

      else if (parsedMessage === "Successfully enabled CommandSpy") commandSpy = true;
      else if (parsedMessage === "Successfully disabled CommandSpy") commandSpy = false;
      else if (parsedMessage === "Successfully disabled CommandSpy.") commandSpy = false;
      else if (parsedMessage === "Successfully enabled CommandSpy.") commandSpy = true;

      else if (parsedMessage?.startsWith("You now have the tag:")) prefix = true;
      else if (parsedMessage === "You no longer have a tag" || parsedMessage === "Something went wrong while saving the prefix. Please check console.") prefix = false;

      else if (parsedMessage === `§6Vanish for ${bot.server.username}: enabled`) vanish = true;
      else if (parsedMessage === `§6Vanish for §c${bot.server.username}§6: disabled`) vanish = false;

      else if (parsedMessage?.startsWith('§6Your nickname is now')) nick = true;
      else if (parsedMessage === "§6You no longer have a nickname.") nick = false;

      else if (parsedMessage?.startsWith("Successfully set your username to ") || parsedMessage?.endsWith(`${bot.server.username}`)) username = true;
      else if (parsedMessage === `Successfully set your username to "${bot.server.username}"`) username = false;
      else if (parsedMessage === `You already have the username "${bot.server.username}"`) username = false;

      } catch (e) {
        console.log(e.stack)
      }
    });

    bot.on('packet.entity_status', (data) => {
      if (data.entityId !== entityId || data.entityStatus < 24 || data.entityStatus > 28) return;
      permission = data.entityStatus - 24
    });

    bot.on('packet.game_state_change', (packet) => {
      if (packet.reason === 3 || packet.reason === "change_game_mode") {
        gameMode = packet.gameMode;
      } if (packet.reason === 4 || packet.reason === "win_game") {
        bot._client.write('client_command', { action: 0 })        
      }
    });


/*    bot.on("packet.position", (packet, position) => {
      if (!config.selfcare.icu || bot.server.mode === "savageFriends") return;
      positionCount++
      setTimeout(() => {
        positionCount--
        if (positionCount > 4) {
          bot.core.run('sudo * icu stop');
        } if (permission < 2 || gameMode !== 1) {
          bot._client.end('anti icu :3');
        }
      }, 1000)
    })*/

    let timer;
    bot.on('packet.login', (data) => {
      entityId = data.entityId;
      switch (data.worldState.gamemode) {
        case 'survival':
          gameMode = 0
          break
        case 'creative':
          gameMode = 1
          break
        case 'adventure':
          gameMode = 2
          break
        case 'spectator':
          gameMode = 3
          break
        default:
          gameMode = 0
          break
      }

//      if (bot.server.mode === "savageFriends") return;
      timer = setInterval(() => {
        if (bot.server.mode === "savageFriends") {
          if (gameMode !== 1 && config.selfcare.game) bot.chat("/minecraft:gamemode creative")
          else if (permission < 2 && config.selfcare.op) bot.chat(`/minecraft:op ${bot._client.username}`)
        } else {
        if (permission < 2 && config.selfcare.op) bot.chat("/minecraft:op @s[type=player]");
        else if (gameMode !== 1 && config.selfcare.gamemode) bot.chat("/minecraft:gamemode creative");
        else if (!commandSpy && config.selfcare.cspy) bot.chat('/cspy on');
        else if (prefix && config.selfcare.prefix) bot.chat('/prefix off');
        else if (!vanish && config.selfcare.vanish) bot.chat('/vanish on');
        else if (username && config.selfcare.username) bot.chat(`/username ${bot.server.username}`);

        else if (mute && config.selfcare.mute) bot.core.run(`essentials:mute ${bot.uuid}`);
        else if (!god && config.selfcare.god) bot.core.run(`god ${bot.server.username} on`);
        else if (nick && config.selfcare.nick) bot.core.run(`nick ${bot.server.username} off`);

        else if (gameMode !== 4) bot._client.write("client_command", { actionId: 0 });
        }
      }, 1000);
    });

    bot.on('end', () => {
      if (timer) clearInterval(timer);
      commandSpy = false;
      god = false;
      mute = false;
      prefix = false;
      vanish = false;
      nick = false;
      username = false;
    });
  }
}
