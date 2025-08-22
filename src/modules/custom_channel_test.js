const channels = "extras:register\0extras:unregister\0extras:message\0";
class custom_channel_test {
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
//    const buf = Buffer.from('meow', 'utf8')
//    buf[buf.length - 1] |= 0x80;
  // I wanna use the extras channels to my advantage --- Parker2991
    bot.extras = {
      channel: "default",
      register (channel) {
        const buf = Buffer.from(channel, "utf8")
        buf[buf.length - 1] |= 0x80;
//        this.channel = buf
        bot._client.write("custom_payload", {
          channel: "extras:register",
          data: buf,
        })

        bot._client.write('custom_payload', {
          channel: "minecraft:register",
          data: buf
        });
      },
      unregister (channel) {
        const buf = Buffer.from(channel, "utf8")
        buf[buf.length - 1] |= 0x80;

        bot._client.write("custom_payload", {
          channel: "extras:unregister",
          data: buf,
        })
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
  }
}

module.exports = custom_channel_test;
