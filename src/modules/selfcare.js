const sleep = require('../util/sleep');

function inject (context) {
  const bot = context.bot;
  const config = context.config;
  const options = context.options;
  let entityId;
  let permissionLevel = 2;
  let unmuted = false;
  let gameMode;
  let commandSpy = false;
  let vanished = false;
  let prefix = false;
  let god = false;
  let teleportToggle = false;
  let username = false;
  let nickname = false;
  let login = false;
  let register = false;
  let positionCount = 0;
  bot.vanished = true
  bot.on('message', (data) => {
    try {
    const stringMessage = bot.getMessageAsPrismarine(data)?.toString();
    if (options.mode === "savageFriends") {
      if (stringMessage === "Please, login with the command: /login <password>") login = true;
      else if (stringMessage === "Successful login!") login = false;
      else if (stringMessage === "You're already logged in!") login = false;
      else if (stringMessage === "Please, register to the server with the command: /register <password> <ConfirmPassword>") register = true;
      else if (stringMessage === "This user isn't registered!") register = true;
      else if (stringMessage === "Successfully registered!") register = false;
      else if (stringMessage === "You're already logged in!") register = false;
      else if (stringMessage === "Successful login!") register = false;
      else if (stringMessage === ` > Prefix for user ${bot.options.username} set to: [Prefix: ${config.prefixes[0]}]`) prefix = true;
      else if (stringMessage?.startsWith(` > Prefix for user ${bot.options.username} set to: `) || stringMessage === ` > Prefix for user ${bot.options.username} removed.`) prefix = false;
      else if (stringMessage === `You no longer have a nickname.`) nickname = false;
      else if (stringMessage?.startsWith('Your nickname is now ')) nickname = true;
      else if (stringMessage === `Vanish for ${bot.options.username}: enabled`) vanished = true;
      else if (stringMessage === `Vanish for ${bot.options.username}: disabled`) vanished = false;
    } else if (options.mode === "kaboom") {
      if (stringMessage === "Successfully enabled CommandSpy") commandSpy = true;
      else if (stringMessage === "Successfully enabled CommandSpy.") commandSpy = true;
      else if (stringMessage === "Successfully disabled CommandSpy") commandSpy = false;
      else if (stringMessage === "Successfully disabled CommandSpy.") commandSpy = false;
      else if (stringMessage === `Vanish for ${bot._client.username}: enabled`) vanished = true;
      else if (stringMessage === `Vanish for ${bot._client.username}: disabled`) vanished = false;
      else if (stringMessage === `You now have the tag: &8[&bPrefix&8: &3${config.prefixes[0]}&8]` || stringMessage === "Something went wrong while saving the prefix. Please check console.") prefix = true;
      else if (stringMessage?.startsWith("You now have the tag: ") || stringMessage === "You no longer have a tag") prefix = false
      else if (stringMessage?.startsWith("You have been muted")) unmuted = true;
      else if (stringMessage?.startsWith("You have been unmuted")) unmuted = false;
      else if (stringMessage?.startsWith("Your voice has been silenced")) unmuted = true;
      else if (stringMessage === "God mode disabled.") god = false;
      else if (stringMessage === "God mode enabled.") god = true;
      else if (stringMessage === "Teleportation disabled.") teleportToggle = true;
      else if (stringMessage === "Teleportation enabled.") teleportToggle = false;
      else if (stringMessage === `Successfully set your username to "${bot._client.username}"`) {
        username = false
        return
      }
      else if (stringMessage?.startsWith("Successfully set your username to ")) username = true
      else if (stringMessage === `You already have the username "${bot._client.username}"`) username = false
      else if (stringMessage === `You no longer have a nickname.`) nickname = false;
      else if (stringMessage?.startsWith('Your nickname is now ')) nickname = true;
    }
    } catch (e) {
      console.log(e.stack);
    }
  })

  bot.on('packet.entity_status', packet => {
    if (packet.entityId !== entityId || packet.entityStatus < 24 || packet.entityStatus > 28) return
    permissionLevel = packet.entityStatus - 24
  })

  bot.on('packet.game_state_change', packet => {
    if (packet.reason !== 3) return // Reason 3 = Change Game Mode
    gameMode = packet.gameMode;
  });

  bot.on("packet.game_state.change", packet => {
    if (packet.reason !== 4) return // checks if the bot is seeing the endcredits or died
    clientLock = packet.gameMode;
  })

  bot.on("packet.position", (packet, position) => {
    if (options.mode === "savageFriends" || options.mode === "creayun") return;
    positionCount++
    setTimeout(() => {
      positionCount--
      if (positionCount > 4) {
        bot._client.end('anti icu')
      }
    }, 1000)
  })

  let timer;
  bot.on('packet.login', async (packet) => {
    entityId = packet.entityId;
    gameMode = packet.gameMode;
    clientLock = packet.gameMode;
    timer = setInterval(() => {
      if (bot.options.mode === "savageFriends") {
        if (login) bot.chat.command('login amogusissus');
        else if (register) bot.chat.command('register amogusissus amogusissus');
        else if (gameMode !== 1) bot.chat.command('minecraft:gamemode creative');
        else if (permissionLevel < 2) bot.chat.command(`minecraft:op ${bot.options.username}`);
        else if (!prefix) bot.chat.command(`rank ${bot.options.username} &8[&bPrefix&8: &3${config.prefixes[0]}&8]`);
        else if (nickname) bot.chat.command('nick off');
        else if (!vanished && bot.vanished) bot.chat.command('essentials:vanish on');
        else if (clientLock !== 4) bot._client.write("client_command", { actionId: 0 });
      } else if (bot.options.isCreayun && !bot.options.isKaboom && !bot.options.isSavage) {

      } else if (bot.options.mode === "kaboom") {
        if (permissionLevel < 2) bot.chat.command('op @s[type=player]');
        else if (gameMode !== 1) bot.chat.command('minecraft:gamemode creative');
        else if (username) bot.chat.command(`username ${bot._client.username}`)
//        else if (nickname) bot.chat.command(`nick off`)
        else if (!prefix) bot.chat.command(`prefix &8[&bPrefix&8: &3${config.prefixes[0]}&8]`);
        else if (!vanished && bot.vanished) bot.core.run(`essentials:vanish ${bot._client.username} on`);
        else if (!commandSpy) bot.core.run(`commandspy ${bot.uuid} on`);
        else if (unmuted) bot.core.run(`essentials:mute ${bot.uuid}`);
        else if (!god) bot.core.run(`god ${bot.options.username} enable`);
        else if (!teleportToggle) bot.core.run(`tptoggle ${bot.options.username} disable`);
        else if (clientLock !== 4) bot._client.write("client_command", { actionId: 0 });
      }
    }, options.selfcareInterval);
  });
  bot.on('end', () => {
    if (timer) clearInterval(timer)
    prefix = false;
    commandSpy = false;
    vanished = false;
    prefix = false;
    god = false;
    unmuted = false;
    username = false;
  });
}

module.exports = {
  data: {
    enabled: true,
    name: "selfcare",
    type: "extras"
  },
  inject
};
