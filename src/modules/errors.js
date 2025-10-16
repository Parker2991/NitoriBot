class errors {
  constructor (context) {
    const bot = context.bot;
    process.on('error', (source, error) => {
      const errorName = error.name;
      switch (errorName)  {
        case "CommandError":
          source.sendFeedback(error._message)
        break;
        case "UnknownCommand":
          source.sendFeedback(error._message)
        break
        case "Error":
          source.sendFeedback({
            translate: "fnfboyfriendbot.error.error",
            fallback: bot.translations["fnfboyfriendbot.error.error"],
            color: "red",
            hover_event: {
              action: "show_text",
              value: String(error.stack)
            }
          })
        break;
        case "TypeError":
          source.sendFeedback({
            translate: "fnfboyfriendbot.error.type_error",
            fallback: bot.translations["fnfboyfriendbot.error.type_error"],
            color: "red",
            hover_event: {
              action: "show_text",
              value: String(error.stack)
            }
          })
        break;
        case "RangeError":
          source.sendFeedback({
            translate: "fnfboyfriendbot.error.range",
            fallback: bot.translations["fnfboyfriendbot.error.range"],
            color: "red",
            hover_event: {
              action: "show_text",
              value: String(error.stack)
            }
          })
        break;
        case "ReferenceError":
          source.sendFeedback({
            translate: "fnfboyfriendbot.error.reference",
            fallback: bot.translations["fnfboyfriendbot.error.reference"],
            color: "red",
            hover_event: {
              action: "show_text",
              value: String(error.stack)
            }
          })
        break;
        case "AssertionError":
          source.sendFeedback({
            translate: "fnfboyfriendbot.error.assertion",
            fallback: bot.translations["fnfboyfriendbot.error.assertion"],
            color: "red",
            hover_event: {
              action: "show_text",
              value: String(error.stack)
            }
          })
        break;
        case "SyntaxError":
          source.sendFeedback({
            translate: "fnfboyfriendbot.error.syntax",
            fallback: bot.translations["fnfboyfriendbot.error.syntax"],
            color: "red",
            hover_event: {
              action: "show_text",
              value: String(error.stack)
            }
          })
        break;
      }
    });
  }
}

module.exports = errors;