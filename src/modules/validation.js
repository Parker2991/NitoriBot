const crypto = require("crypto");

class validation {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    bot.validation = {}
    setInterval(() => {
      bot.validation.trusted = crypto.createHash("sha512").update(Math.floor(Date.now() / 2000) + config.keys.trusted).digest("hex").substring(0, 16)
      bot.validation.admin = crypto.createHash("sha512").update(Math.floor(Date.now() / 2000) + config.keys.admin).digest("hex").substring(0, 16)
      bot.validation.owner = crypto.createHash("sha512").update(Math.floor(Date.now() / 2000) + config.keys.owner).digest("hex").substring(0, 16)
    }, 100)
  }
}
module.exports = validation;
