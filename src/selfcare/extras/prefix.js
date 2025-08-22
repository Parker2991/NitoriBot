class prefixSelfcare {
  constructor(context) {
    const bot = context.bot;
    const options = context.options;
    const stringMessage = context.stringMessage;
    const config = context.config;

    if (options.mode === "savageFriends") {
    } else {
      if (
        stringMessage ===
          `You now have the tag: &8[&bPrefix&8: &3${config.prefixes[0]}&8]` ||
        stringMessage ===
          "Something went wrong while saving the prefix. Please check console."
      )
        bot.selfcare.prefix = true;
      else if (
        stringMessage?.startsWith("You now have the tag: ") ||
        stringMessage === "You no longer have a tag"
      )
        bot.selfcare.prefix = false;
    }
  }
}

module.exports = prefixSelfcare;
