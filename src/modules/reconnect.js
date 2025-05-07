const mc = require("minecraft-protocol");
const usernameGen = require("../util/usernameGen");
class ReconnectModule {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    const options = context.options;
    let client
    bot.reconnectDelay = options.reconnectDelay;

    bot.on("end", () => {
      bot._client.removeAllListeners();
      setTimeout(() => {
        if (options.usernameGen) {
          client =
            options.client ??
            mc.createClient(options, (options.username = new usernameGen().username()));
        } else {
          client = options.client ?? mc.createClient(options);
        }
        bot._client = client;
        bot.emit("init_client", bot._client);
      }, bot.reconnectDelay);
    });
  }
}
module.exports = ReconnectModule;
