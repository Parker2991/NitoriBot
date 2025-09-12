class command_spy {
  constructor(context) {
    const bot = context.bot;

    bot.on("system_chat", (message) => {
      try {
        const child = message.extra;
        const sender =
          bot.players.find((e) => e?.profile?.name === message.text)?.profile
            ?.name === message.text;
        if (!child || !message.text || !message.color || !sender) return;
        const command = child[1].substring("/".length);

        if (
          (message.color === "yellow" &&
            child[0] === ": " &&
            child[1] === `/${command}` &&
            sender) ||
          (message.color === "aqua" &&
            child[0] === ": " &&
            child[1] === `/${command}` &&
            sender)
        ) bot.emit("parsed_message", {
            sender: bot.players.find((e) => e.profile.name === message.text),
            contents: { text: command },
            type: "extras:command_spy",
            displayName: bot.players.find(
              (e) => e.profile.name === message.text,
            ).displayName,
          });
      } catch (e) {
        console.log(e.stack);
      }
    });
    
  }
}

module.exports = command_spy;
