const { stylize } = require('../util/eval_colors');
const util = require('util');

module.exports = {
  data: {
    name: "eval",
    enabled: true,
    aliases: [
    ],
    description: "self explanatory",
    usages: [
      "<input>"
    ]
  },
  execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    try {
      bot.console.command(util.inspect(eval(args.join(' ')), { stylize }));
    } catch (e) {
      bot.console.command(e.toString());
    }
  }
}
