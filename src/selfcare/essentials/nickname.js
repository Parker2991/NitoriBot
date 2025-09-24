class nickname {
  constructor(context) {
    const bot = context.bot;
    const options = context.options;
    const stringMessage = context.stringMessage;

    if (stringMessage === `You no longer have a nickname.`)
      bot.selfcare.nickname = false;
    else if (stringMessage?.startsWith("Your nickname is now "))
      bot.selfcare.nickname = true;
  }
}

module.exports = nickname;
