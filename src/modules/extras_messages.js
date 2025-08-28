const channels = "extras:register\0extras:unregister\0extras:message\0";
class extras_message {
  constructor (context) {
    const bot = context.bot
    const config = context.config
    const options = context.options

    bot.on('packet.login', () => {
      bot._client.write("custom_payload", {
        channel: "minecraft:register",
        data: Buffer.from(channels, "utf8")
      })
    });

  // I wanna use the extras channels to my advantage --- Parker2991
    bot.extras = {
      channels: [],
      register (channel) {
        const buf = Buffer.from(channel, "utf8")
        buf[buf.length - 1] |= 0x80;
        bot._client.write("custom_payload", {
          channel: "extras:register",
          data: buf,
        })

        bot.extras.channels.push(channel)
      },
      unregister (channel) {
        const buf = Buffer.from(channel, "utf8")
        buf[buf.length - 1] |= 0x80;

        bot._client.write("custom_payload", {
          channel: "extras:unregister",
          data: buf,
        })
        bot.extras.channels.pop(channel)
      },
      unregisterall () {
        for (const channel of this.channels) {
          this.unregister(channel)
        }
      },
      send (channel, message) {
        const buf = Buffer.from(channel, "utf8")
        buf[buf.length - 1] |= 0x80;
        bot._client.write("custom_payload", {
          channel: "extras:message",
          data: Buffer.concat([buf, Buffer.from(`${message}`, "utf8")])
        })
      }
    }

    bot.on('end', () => bot.extras.channels = [])

    bot.on('packet.custom_payload', (data) => {
      if (data.channel === "extras:message") {
        console.log(data.data.toString('ascii'))
      }
    })
  }
}

module.exports = extras_message;
