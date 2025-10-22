const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");
const wikipedia = require('wikipedia');

class wiki extends CommandContext {
  constructor() {
    super(
      "wiki",
      [
        "wikipedia"
      ],
      "look up articles on wikipedia",
      trustLevel.public,
      ["<article>"],
      true,
    );
  }

  async execute(context) {
    const source = context.source;
    const args = context.arguments;
    const bot = context.bot;
    const config = context.config;
    const translations = bot.translations;
    let component = [];
    try {
      const page = await wikipedia.page(args.join(' '));
      const summary = await page.intro();
      component.push({ text: summary, color: config.colors.commands.primary })

      source.sendFeedback(component)
    } catch (e) {
      if (e.toString().startsWith('pageError: pageError: No page with given title exists :')) {
        source.sendFeedback({
          translate: "fnfboyfriendbot.command.wiki.article_not_found",
          fallback: translations["fnfboyfriendbot.command.wiki.article_not_found"],
          color: "red"
        })
      } else {
        source.sendFeedback(e.stack)
      }
    }
  }
}

module.exports = wiki;