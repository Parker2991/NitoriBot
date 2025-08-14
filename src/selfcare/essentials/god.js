class godSelfcare {
  constructor (context) {
    const bot = context.bot;
    const options = context.options;
    const stringMessage = context.stringMessage;

    if (stringMessage === "God mode disabled.") bot.selfcare.god = false;
    else if (stringMessage === "God mode enabled.") bot.selfcare.god = true;

  }
}

module.exports = godSelfcare