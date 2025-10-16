const CommandError = require('./CommandError');
const translations = require('../data/translations.json');

class CommandArguments {
  static getInteger (integer) {
    if (isNaN(integer)) throw new CommandError({
      translate: "fnfboyfriendbot.arguments.invalid_integer",
      fallback: translations["fnfboyfriendbot.arguments.invalid_integer"],
      color: "red",
      with: [
        { text: `${integer}` }
      ]
    })
    else return String(Number(integer))
  }
  
  static getJson (json) {

  }

  static getArguments () {

  }

  static getMaxArguments (commandArgs, args) {
    if (commandArgs < args) throw new CommandError({
      translate: "fnfboyfriendbot.arguments.max_exceeded",
      fallback: translations["fnfboyfriendbot.arguments.max_exceeded"],
      color: "red",
      with: [
        { text: `${commandArgs}` },
        { text: `${args}` }
      ]
    })
  }
}

module.exports = CommandArguments;