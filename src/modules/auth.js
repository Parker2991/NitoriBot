class auth {
  constructor(context) {
    const bot = context.bot;
    const options = context.options;
    const config = context.config;

    bot.auth = {
      list: [],
      find(player) {
        this.list.find();
      },
      add(player) {
        this.list.push(player);
      },
      remove(player) {
        this.list.splice(player, 1);
      },
      clear() {
        this.list = [];
      },
    };

    bot.on("end", () => bot.auth.clear());

    bot.on("player_left", (data) => {
      const players = bot.auth.list.find((e) => e?.player === data[0])?.player;
      if (players === data[0]) bot.auth.remove(data);
    });
  }
}

module.exports = auth;
