const CommandError = require("../../command_util/command_error");
const { request } = require("undici");
const CommandContext = require("../../command_util/command_context");

class UrbanCommand extends CommandContext {
  constructor() {
    super("urban", ["urbandictionary"], "urban dictionary", 0, [
      "<definition>",
    ]);
  }
  async execute(context) {
    const source = context.source;
    const args = context.arguments;
    const bot = context.bot;
    const prefix = [
      { text: "[", color: "dark_gray" },
      { text: "Urban", color: "#B72A00" },
      { text: "] ", color: "dark_gray" },
    ];
    let component = [];
    let term = `${args.join(" ")}`;
    const query = new URLSearchParams({ term });
    const dictResult = await request(
      `https://api.urbandictionary.com/v0/define?${query}`,
    );
    const { list } = await dictResult.body.json();

    if (bot.options.mode === "savageFriends") {
      bot.chat.message(
        "this command is disabled for this server for right now sorry!",
      );
      return;
    }

    if (!list.length) {
      bot.tellraw("@a", { text: "No results found", color: "dark_red" });
    }

    for (definitions of list) {
      component.push(prefix, [
        {
          text: `${definitions.definition.replaceAll("\r", "").replaceAll("[", "\xa71\xa7n\xa7o").replaceAll("]", "\xa7r\xa77")}\n`,
          color: "gray",
          underlined: false,
          italic: false,
          translate: "",
          hoverEvent: {
            action: "show_text",
            value: [
              {
                text: `Example \u203a \n ${definitions.example.replaceAll("\r", "").replaceAll("[", "\xa71\xa7n\xa7o").replaceAll("]", "\xa7r\xa77")}\n`,
                color: "gray",
              },
              {
                text: `Word \u203a ${definitions.word.replaceAll("\r", "").replaceAll("[", "\xa71\xa7n\xa7o").replaceAll("]", "\xa7r\xa77")}\n`,
                color: "gray",
              },
              {
                text: `Author \u203a ${definitions.author.replaceAll("\r", "").replaceAll("[", "\xa71\xa7n\xa7o").replaceAll("]", "\xa7r\xa77")}\n`,
                color: "gray",
              },
              {
                text: `written on \u203a ${definitions.written_on.replaceAll("\r", "").replaceAll("[", "\xa71\xa7n\xa7o").replaceAll("]", "\xa7r\xa77")}\n`,
                color: "gray",
              },
              {
                text: `Rating \u203a Thumbs-Up ${definitions.thumbs_up} / Thumbs-Down ${definitions.thumbs_down}`,
                color: "gray",
              },
            ],
          },
          clickEvent: {
            action: "open_url",
            value: `${definitions.permalink}`,
          },
        },
      ]);
    }

    if (source.sources.console) {
      bot.console.info(bot.getMessageAsPrismarine(component)?.toAnsi());
    } else if (bot.options.mode === "savageFriends") {
      bot.core.run(`minecraft:tellraw @a ${JSON.stringify(component)}`);
    } else {
      bot.tellraw(`@a`, component);
    }
  }
};

module.exports = UrbanCommand