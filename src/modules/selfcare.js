const fs = require('fs')
const path = require('path')

class selfcare {
  constructor (context) {
    const bot = context.bot
    const config = context.config
    const options = context.options

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
      position_count: 0,

    }

    let selfcare = [];

    for (const file of fs.readdirSync(path.join(__dirname, '../selfcare/extras'))) {
      try {
        const extrasSelfcare = require(path.join(__dirname, '../selfcare/extras', file))
        selfcare.push(extrasSelfcare)
      } catch (e) {
        console.error(e.stack)
      }
    }

    for (const file of fs.readdirSync(path.join(__dirname, '../selfcare/essentials'))) {
      try {
        const essentialsSelfcare = require(path.join(__dirname, '../selfcare/essentials', file))
        selfcare.push(essentialsSelfcare)
      } catch (e) {
        console.error(e.stack)
      }
    }

    for (const file of fs.readdirSync(path.join(__dirname, '../selfcare/entity'))) {
      try {
        const loadEntitySelfcare = require(path.join(__dirname, '../selfcare/entity', file))

        new loadEntitySelfcare({ bot, config, options })
      } catch (e) {
        console.error(e.stack)
      }
    }

    bot.on('system_chat', (message) => {
      const stringMessage = bot.getMessageAsPrismarine(message)?.toString()
      
      for (const file of selfcare) new file({ bot, config, options, stringMessage })

    })
    let timer;
    bot.on('packet.login', (data) => {
      bot.selfcare.entityId = data.entityId;
      bot.selfcare.gameMode = data.gameMode;
      
      timer = setInterval(() => {
        if (bot.options.mode === "savageFriends") {

        } else {
          if (!bot.selfcare.prefix) bot.chat.command(`extras:prefix &8[&bPrefix&8: &3${config.prefixes[0]}&8]`);
          else if (bot.selfcare.permissionLevel < 2) bot.chat.command('minecraft:op @s[type=player]')
          else if (bot.selfcare.username) bot.chat.command(`extras:username ${bot._client.username}`)
          else if (bot.selfcare.gameMode !== 1) bot.chat.command('minecraft:gamemode creative')
          else if (!bot.selfcare.commandSpy) bot.core.run(`commandspy ${bot.uuid} on`)
          else if (!bot.selfcare.vanished) bot.core.run(`essentials:vanish ${bot._client.username} on`)
          else if (!bot.selfcare.god) bot.core.run(`essentials:god ${bot._client.username} on`)
          else if (bot.selfcare.mute) bot.core.run(`essentials:mute ${bot._client.uuid}`)
          else if (bot.selfcare.nickname) bot.core.run(`essentials:nickname ${bot._client.username} off`)
          
        }
      }, options.selfcareInterval)
    })

    bot.on('end', () => {
      if (timer) clearInterval(timer)
      bot.selfcare.commandSpy = false
      bot.selfcare.prefix = false
      bot.selfcare.vanished = false
      bot.selfcare.mute = false
      bot.selfcare.username = false
      bot.selfcare.nickname = false
      bot.selfcare.god = false

    })
  }
}
module.exports = selfcare