module.exports = {
  data: {
    description: "loops commands",
    enabled: true,
    name: "command loop",
    type: "extras"
  },
  inject (context) {
    const bot = context.bot;
    bot.cloop = {
      list: [],
      add (command, interval) {
        let timer;
        if (bot.server.mode === "savageFriends") {
          timer = ({ timer: setInterval(() => bot.chat(`${command}`), interval), command, interval })
        } else {
          timer = ({ timer: setInterval(() => bot.core.run(command), interval), command, interval })
        }
        this.list.push(timer)

        bot.on('end', () => {
          this.clear()
        })

        bot.on('packet.login', () => {
          if (this.list.length === 0) return
          timer = ({ timer: setInterval(() => bot.core.run(command), interval), command, interval })
          this.list.push(timer);
        })
      },

      remove (index) {
        clearInterval(this.list[index].timer)
        bot.cloop.list.splice(index, 1)
      },

      clear () {
        for (const cloop of this.list) clearInterval(cloop.timer)
        this.list = []
      }
    }
  }
}
