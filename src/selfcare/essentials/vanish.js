class vanishSelfcare {
  constructor (context) {
    const bot = context.bot;
    const options = context.options;
    const stringMessage = context.stringMessage;

    if (stringMessage === `Vanish for ${bot._client.username}: enabled`) bot.selfcare.vanished = true;
    else if (stringMessage === `Vanish for ${bot._client.username}: disabled`) bot.selfcare.vanished = false;
  }
}

module.exports = vanishSelfcare