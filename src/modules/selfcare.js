const fs = require('fs')
const path = require('path')

class selfcare {
  constructor (context) {
    const bot = context.bot
    const config = context.config
    const options = context.options
    let messageSelfcare
    let loadMessageSelfcare

    bot.selfcare = {
      permissionLevel: 2,
      entityId: undefined,
      gameMode: undefined,
      commandSpy: undefined
    }

    for (const file of fs.readdirSync(path.join(__dirname, '../selfcare/message'))) {
      try {
        loadMessageSelfcare = require(path.join(__dirname, '../selfcare/message', file))
      } catch (e) {
        console.error(e.stack)
      }
    }

    for (const file of fs.readdirSync(path.join(__dirname, '../selfcare/entity'))) {
      try {
        const loadEntitySelfcare = require(path.join(__dirname, '../selfcare/entity', file))
        const entitySelfcare = new loadEntitySelfcare({ bot, config, options })
      } catch (e) {
        console.error(e.stack)
      }
    } // how the fuck do i want to implement this...

    bot.on('message', (message) => {
      const stringMessage = bot.getMessageAsPrismarine(message)?.toString()
      //new loadMessageSelfcare({ bot, options, config, stringMessage })
    })
    let timer;
    bot.on('packet.login', (data) => {
      bot.selfcare.entityId = data.entityId;
      if (bot.options.version === "1.21" || bot.options.version === "1.21.1") {
        switch (data.worldState.gamemode) {
          case "survival":
            bot.selfcare.gameMode = 0;
            break;
          case "creative":
            gameMode = 1;
            break;
          case "adventure":
            bot.selfcare.gameMode = 2;
            break;
          case "spectator":
            bot.selfcare.gameMode = 3;
            break;
          default:
            bot.selfcare.gameMode = 0;
            break;
        }
      } else {
        bot.selfcare.gameMode = data.gameMode;
      }
      timer = setInterval(() => {
        if (bot.selfcare.permissionLevel < 2) bot.chat.command('minecraft:op @s[type=player]')
        else if (bot.selfcare.gameMode !== 1) bot.chat.command('minecraft:gamemode creative')
        //else if (!bot.selfcare.commandSpy) bot.core.run(`commandspy ${bot.uuid} on`)
      }, options.selfcareInterval)
    })

    bot.on('end', () => {
      if (timer) clearInterval(timer)
      bot.selfcare.commandSpy = false
    })
  }
}
module.exports = selfcare