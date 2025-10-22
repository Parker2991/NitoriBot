const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");
const { request } = require('undici');

class urban extends CommandContext {
  constructor() {
    super(
      "urban",
      [
        "urbandictionary"
      ],
      "look up definitions on urban dictionary",
      trustLevel.public,
      ["<definition>"],
      true,
    );
  }

  async execute(context) {
    const bot = context.bot;
    const source = context.source;
    const args = context.arguments;
    const config = context.config;
    const translations = bot.translations;
    let term = args.join(' ');

    try {
      const prefix = {
        translate: "[%s] ",
        color: config.colors.commands.primary,
        with: [
          {
            translate: "fnfboyfriendbot.command.urban.text",
            fallback: translations["fnfboyfriendbot.command.urban.text"],
            color: "#B72A00"
          }
        ]
      }

      const query = new URLSearchParams({ term });
      const dictResult = await request(`https://api.urbandictionary.com/v0/define?${query}`);
      const { list } = await dictResult.body.json();
      let component = [];

      for (const definitions of list) {
        component.push(prefix, [
          {
            text: `${definitions.definition.replaceAll('\r','').replaceAll('[', '\xa71\xa7n\xa7o').replaceAll(']','\xa7r\xa77')}\n`,
            color: 'gray',
            underlined: false,
            italic: false,
            translate: ""
          }
        ])
      }
      source.sendFeedback(component)
    } catch (e) {
      bot.console.error(e.stack)
    }
  }
}

module.exports = urban;

/*
const prefix = [
      { text: '[', color: 'dark_gray' },
      { text: 'Urban', color: '#B72A00' },
      { text: '] ', color: 'dark_gray'}
    ]
   let component = [];
   let term = `${args.join(' ')}`
   const query = new URLSearchParams({ term });
   const dictResult = await request(`https://api.urbandictionary.com/v0/define?${query}`);
   const { list } = await dictResult.body.json();

   if (bot.options.mode === "savageFriends") {
     bot.chat.message('this command is disabled for this server for right now sorry!');
     return;
   }

   if (!list.length) {
     bot.tellraw("@a", { text: "No results found", color: "dark_red" });
   }


   for (definitions of list) {
     component.push(prefix, [
       {
         text: `${definitions.definition.replaceAll('\r','').replaceAll('[', '\xa71\xa7n\xa7o').replaceAll(']','\xa7r\xa77')}\n`,
         color: 'gray',
         underlined: false,
         italic: false,
         translate: "",
         hover_event: {
           action:"show_text",
           value: [
             {
               text: `Example \u203a \n ${definitions.example.replaceAll('\r', '').replaceAll('[', '\xa71\xa7n\xa7o').replaceAll(']','\xa7r\xa77')}\n`,
               color: 'gray'
             },
             {
               text: `Word \u203a ${definitions.word.replaceAll('\r', '').replaceAll('[', '\xa71\xa7n\xa7o').replaceAll(']','\xa7r\xa77')}\n`,
               color: 'gray',
             },
             {
               text: `Author \u203a ${definitions.author.replaceAll('\r', '').replaceAll('[', '\xa71\xa7n\xa7o').replaceAll(']','\xa7r\xa77')}\n`,
               color: 'gray'
             },
             {
               text: `written on \u203a ${definitions.written_on.replaceAll('\r', '').replaceAll('[', '\xa71\xa7n\xa7o').replaceAll(']','\xa7r\xa77')}\n`,
               color: 'gray'
             },
             {
               text: `Rating \u203a Thumbs-Up ${definitions.thumbs_up} / Thumbs-Down ${definitions.thumbs_down}`,
               color: 'gray'
             }
           ]
         },
         click_event: {
           action: 'open_url',
           value: `${definitions.permalink}`
         }
       },
     ])
*/