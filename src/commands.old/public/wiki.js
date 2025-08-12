const wiki = require("wikipedia");
const CommandError = require("../../command_util/command_error");
const fixansi = require("../../util/ansi");
const CommandContext = require("../../command_util/command_context");

class WikiCommand extends CommandContext {
  constructor() {
    super("wiki", ["wikipedia"], "wikipedia", 0, ["<article>"]);
  }
  async execute(context) {
    const source = context.source;
    const args = context.arguments;
    const bot = context.bot;
    const config = context.config;
    let component = [];

    try {
      const page = await wiki.page(args.join(" "));
      const summary = await page.intro();

      component.push({ text: summary, color: config.colors.commands.primary });

      if (source.sources.console) {
        bot.console.info(bot.getMessageAsPrismarine(component)?.toAnsi());
      } else if (source.sources.discord) {
        bot.discord.message.reply(
          `\`\`\`ansi\n${new fixansi(bot.getMessageAsPrismarine(component)?.toAnsi()).ansi}\`\`\``,
        );
      } else {
        bot.tellraw("@a", component);
      }
    } catch (e) {
      if (
        e.toString() ===
        "pageError: TypeError: Cannot read properties of undefined (reading 'pages')"
      ) {
        bot.tellraw("@a",
          { text: "no article found!", color: "red" }
        )
      } else {
        bot.tellraw(`@a`, `${e.toString()}`);
      }
    }
  }
}

module.exports = WikiCommand;
