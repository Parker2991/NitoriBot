module.exports = {
  data: {
    descripton: "logs chat to console",
    enabled: true,
    name: "log",
    type: "console"
  },
  inject (context) {
    const bot = context.bot;

    bot.on('message', (message) => {
      try {
        bot.console.log(message)
      } catch {}
    })
  }
}

