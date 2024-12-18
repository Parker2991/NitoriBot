module.exports = {
  data: {
    description: "pretty self explanatory",
    enabled: true,
    name: "custom chat",
    type: "extras"
  },
  inject (context) {
    const bot = context.bot;
    bot.customChat = {
      enabled: true,
      chat (args) {
        const prefix = {
          translate: "chat.type.text",
          with: [
            { selector: `${bot.username}` },
            {
              text: '',
              extra: [ args ]
            }
          ]
        }
        bot.tellraw("@a", prefix);
      }
    }
  }
}

