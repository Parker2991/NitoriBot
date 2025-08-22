class icuSelfcare {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    const options = context.options;

    bot.on("packet.position", (packet, position) => {
      if (options.mode === "savageFriends" || options.mode === "creayun")
        return;
      bot.selfcare.position_count++;
      setTimeout(() => {
        bot.selfcare.position_count--;
        if (bot.selfcare.position_count > 4) {
          bot._client.end("anti icu");
        }
      }, 1000);
    });
  }
}

module.exports = icuSelfcare;
