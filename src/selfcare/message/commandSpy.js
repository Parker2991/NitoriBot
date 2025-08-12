class commandSpySelfcare {
  constructor (context) {
    const bot = context.bot
    const stringMessage = context.stringMessage

    if (stringMessage === "Successfully enabled CommandSpy")
      bot.selfcare.commandSpy = true;
    else if (stringMessage === "Successfully enabled CommandSpy.")
        bot.selfcare.commandSpy = true;
    else if (stringMessage === "Successfully disabled CommandSpy")
      bot.selfcare.commandSpy = false;
    else if (stringMessage === "Successfully disabled CommandSpy.")
      bot.selfcare.commandSpy = false;
  }
}

module.exports = commandSpySelfcare