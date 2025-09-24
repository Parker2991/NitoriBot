const fs = require("fs");
const path = require("path");

class selfcare {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    const options = context.options;

    bot.selfcare = {
      permissionLevel: 2,
      entityId: undefined,
      gameMode: undefined,
      commandSpy: undefined,
      prefix: undefined,
      vanished: undefined,
      mute: undefined,
      username: undefined,
      nickname: undefined,
      god: undefined,
      register: undefined,
      login: undefined,
      position_count: 0,
    };

    const messageSelfcare = [
      require('../selfcare/extras/commandSpy'),
      require('../selfcare/essentials/god'),
      require('../selfcare/essentials/mute'),
      require('../selfcare/auth/login'),
      require('../selfcare/auth/register'),
      require('../selfcare/essentials/vanish')
    ]

    const entitySelfcare = [
      require('../selfcare/entity/gamemode'),
      require('../selfcare/entity/icu'),
      require('../selfcare/entity/op'),
      require('../selfcare/extras/prefix'),
      require('../selfcare/extras/username'),
      require('../selfcare/entity/death')
      //require('../selfcare/essentials/vanish')
    ]
    for (const entity of entitySelfcare) new entity({ bot, config, options });

    bot.on("system_chat", (message) => {
      const stringMessage = bot.getMessageAsPrismarine(message)?.toString();

      for (const file of messageSelfcare)
        new file({ bot, config, options, stringMessage });
    });
    let timer;
    bot.on("packet.login", (data) => {
      bot.selfcare.entityId = data.entityId;
      bot.selfcare.gameMode = data.gameMode;
      switch (data.worldState.gamemode) {
        case 'survival':
          bot.selfcare.gameMode = 0
        break
        case 'creative':
          bot.selfcare.gameMode = 1
        break
        case 'adventure':
          bot.selfcare.gameMode = 2
        break
        case 'spectator':
          bot.selfcare.gameMode = 3
        break
        default:
          bot.selfcare.gameMode = 0
        break
      }
      timer = setInterval(() => {
        if (bot.options.mode === "savageFriends") {
          if (bot.selfcare.register) bot.chat.command('register amogusissus amogusissus')
          else if (bot.selfcare.login) bot.chat.command('login amogusissus')
          else if (bot.selfcare.permissionLevel < 2)
            bot.chat.command(`minecraft:op ${bot._client.username}`);
          else if (bot.selfcare.gameMode !== 1)
            bot._client.write('change_gamemode', { mode: 1 })
        } else if (bot.options.mode === "kaboom") {
          if (!bot.selfcare.prefix)
            bot.chat.command(
              `extras:prefix &8[&bPrefix&8: &3${config.prefixes[0]}&8]`,
            );
          else if (bot.selfcare.permissionLevel < 2)
            bot.chat.command("minecraft:op @s[type=player]");
          else if (bot.selfcare.username) bot.chat.command(`extras:username ${bot._client.username}`)
          else if (bot.selfcare.gameMode !== 1)
            if (bot.options.chatGamemodeChange) bot.chat.command("minecraft:gamemode creative")
            else bot._client.write("change_gamemode", { mode: 1 })
          else if (!bot.selfcare.commandSpy)
            bot.core.run(`commandspy ${bot.uuid} on`);
          else if (!bot.selfcare.vanished)
            bot.core.run(`essentials:vanish ${bot._client.username} on`);
          else if (!bot.selfcare.god)
            bot.core.run(`essentials:god ${bot._client.username} on`);
          else if (bot.selfcare.mute)
            bot.core.run(`essentials:mute ${bot._client.uuid}`);
          else if (bot.selfcare.nickname)
            bot.core.run(`essentials:nickname ${bot._client.username} off`);
        }
      }, options.selfcareInterval);
    });

    bot.on("end", () => {
      if (timer) clearInterval(timer);
      bot.selfcare.commandSpy = false;
      bot.selfcare.prefix = false;
      bot.selfcare.vanished = false;
      bot.selfcare.mute = false;
      bot.selfcare.username = false;
      bot.selfcare.nickname = false;
      bot.selfcare.god = false;
    });
  }
}
module.exports = selfcare;
