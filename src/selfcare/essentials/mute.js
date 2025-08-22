class muteSelfcare {
  constructor(context) {
    const bot = context.bot;
    const options = context.options;
    const stringMessage = context.stringMessage;

    if (stringMessage?.startsWith("You have been muted"))
      bot.selfcare.mute = true;
    else if (stringMessage?.startsWith("You have been unmuted"))
      bot.selfcare.mute = false;
    else if (stringMessage?.startsWith("Your voice has been silenced"))
      bot.selfcare.mute = true;
  }
}

module.exports = muteSelfcare;
