const translations = require('../data/translations.json');

class json {
  static parse (json) {
    try {
      return JSON.parse(json)
    } catch {
      return {
        translate: "fnfboyfriendbot.json.parse_invalid",
        fallback: translations["fnfboyfriendbot.json.parse_invalid"],
        color: "red"
      }
    }
  }

  static stringify (json) {
    try {
      const stringify = JSON.stringify(json);
      if (stringify.length > 32681) return JSON.stringify({
        translate: "fnfboyfriendbot.json.stringify.too_long",
        fallback: translations["fnfboyfriendbot.json.stringify.too_long"],
        color: "red",
      })
      else return stringify;
    } catch {
      return JSON.stringify({
        translate: "fnfboyfriendbot.json.stringify.invalid",
        fallback: translations["fnfboyfriendbot.json.stringify.invalid"],
        color: "red"
      })
    }
  }
}
module.exports = json