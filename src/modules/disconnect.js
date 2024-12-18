
module.exports = {
  data: {
    description: "logs disconnect messages",
    enabled: true,
    name: "disconnect",
    type: "client"
  },

  inject (context) {
    const bot = context.bot;
    const config = context.config;
    bot.on("disconnect", (data) => {

    });

    bot.on("end", (data) => {

    });

    bot.on("error", (data) => {

    });

    bot.on("kick_disconnect", (data) => {

    });
  }
}
