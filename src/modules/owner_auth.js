const sleep = require('../util/sleep');
const decryption = require('../SkiBot/decryption')
const CommandSource = require('../command_util/command_source')

class owner_auth {
  constructor (context) {
    const bot = context.bot;
    const config = context.config;
    const options = context.options;
    const prefixes = config.prefixes;

    if (bot.options.mode !== "kaboom") return
    bot.SkiBotAuthed = false
    let timer
    bot.on('packet.login', async () => {
      const buf = Buffer.from(config.skibot.channel, "utf8")
      buf[buf.length - 1] |= 0x80;
      bot._client.write("custom_payload", {
        channel: "extras:register",
        data: buf,
      })

      if (!config.skibot.enabled) return;
      timer = setInterval(() => {
        if (
          bot.players.find((player) => player.profile.name === config.skibot.player)?.profile?.name === config.skibot.player
          &&
          !bot.SkiBotAuthed
        ) {
          kick(config.skibot.player) 
        }
      }, 3000)
    })

    bot.on('player_left', (data) => {
      if (data.profile.name === config.skibot.player) bot.SkiBotAuthed = false
    })

    bot.on('end', () => {
      if (config.skibot.enabled) clearInterval(timer)
    })
    bot.on('packet.custom_payload', (data) => {
      try {
      if (data.channel === "extras:message") {
        const decoded = data.data.toString('ascii')
        if (decoded.startsWith(config.skibot.channel)) {
          const string = decoded.substring(36)
          const decrypted = decryption.decrypt({ config, encrypted: string })
          const parsed = JSON.parse(decrypted)
          const player = parsed.player;
          const uuid = parsed.uuid;
          const hash = parsed.hash;
          const args = parsed.args;
          if (
            bot.players.find((player) => player.profile.name === config.skibot.player).profile.name === config.skibot.player
            &&
            hash === bot.validation.owner
            &&
            !bot.SkiBotAuthed
          ) bot.SkiBotAuthed = true
        }
      }
    } catch (e) {
      bot.console.error(e.stack)
    }
    })

    function kick (player) {
      bot.core.run(`minecraft:deop @a[name="${player}"]`)
      bot.core.run(`minecraft:gamemode adventure @a[name="${player}"]`)
      bot.core.run(`essentials:mute ${player} 10y not authed`)
      bot.exploits.kicks.burger(`@a[name="${player}"]`)
    }
  }
}

module.exports = owner_auth