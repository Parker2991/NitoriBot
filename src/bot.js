const mc = require("minecraft-protocol");
const { EventEmitter } = require("events");
const ChatMessage = require("prismarine-chat");
const usernameGen = require("./util/usernameGen");
let disconnectCount = 0;

EventEmitter.defaultMaxListeners = Infinity;

class Bot {
  constructor(options = {}, config) {
    const bot = new EventEmitter();
    bot.options = {
      // Set some default values in options
      host: (options.host ??= "localhost"),
      username: (options.username ??= usernameGen()),
      version: (options.version ??= "1.21.8"),
      hideErrors: (options.hideErrors ??= true), // HACK: Hide errors by default as a lazy fix to console being spammed with them
      serverName: (options.serverName ??= "localhost"),
      private: (options.private ??= false),
      reconnectDelay: (options.reconnectDelay ??= 300),
      channelId: (options.channelId ??= "0000000000000000000"),
      logging: {
        file: (options.logging.file ??= false),
        console: (options.logging.console ??= false)
      },
      mode: (options.mode ??= "kaboom"),
      selfcareInterval: (options.selfcareInterval ??= 1000),
      itemRefill: (options.itemRefill ??= false),
      chatGamemodeChange: (options.chatGamemodeChange ??= false)
    };

    bot.options = options;

    // Create our client object, put it on the bot, and register some events
    bot.on("init_client", (client) => {
      client.on("packet", (data, meta) => {
        bot.emit("packet", data, meta);
        bot.emit("packet." + meta.name, data);
      });

      client.once("login", (data) => {
        bot.uuid = client.uuid;
        bot.username = client.username;
        bot.loggedIn = true;
        disconnectCount = 0
        bot.reconnectDelay = options.reconnectDelay;
      });

      client.on("disconnect", (data) => {
        try {
          if (
            JSON.parse(data.reason) ===
            "Wait 5 seconds before connecting, thanks! :)"
          )
            bot.reconnectDelay = 5000;
          bot.console.warn(
            `${ChatMessage(bot._client.version).fromNotch("§8[§bClient Reconnect§8]§r")?.toAnsi()} ${ChatMessage(bot._client.version).fromNotch(data.reason)?.toAnsi()}`,
          );
          bot?.discord?.channel?.send(
            ChatMessage(bot._client.version).fromNotch(data.reason)?.toString(),
          );
        } catch {} // do nothing
      });

      client.on("end", (reason) => {
        bot.emit("end", reason);
        bot.loggedIn = false;
        disconnectCount++;
      });

      client.on("error", (error) => {
        try {
          if (disconnectCount === 10) {
            bot.console.info("stopped logging disconnect messages for now...");
            bot?.discord?.channel?.send(
              "stopped logging disconnect messages for now...",
            );
            return;
          } else if (disconnectCount > 10) {
            return;
          } else {
            bot.console.warn(
              ChatMessage(bot._client.version)
                .fromNotch(`§8[§bClient Reconnect§8]§r ${error.stack}`)
                ?.toAnsi(),
            );
            bot?.discord?.channel?.send(error.toString());
          }
        } catch (e) {
          console.log(e.stack);
        }
      });

      client.on("kick_disconnect", (data) => {
        bot.console.warn(
          `${ChatMessage(bot._client.version).fromNotch("§8[§bClient Reconnect§8]§r")?.toAnsi()} ${ChatMessage(bot._client.version).fromNotch(data.reason)?.toAnsi()}`,
        );
        bot?.discord?.channel?.send(
          ChatMessage(bot._client.version).fromNotch(data.reason)?.toString(),
        );
      });
    });

    process.on("uncaughtException", (e) => {
      //console.log('meow')
      console.warn(`${e.stack}`);
    });


    const client = options.client ?? new mc.createClient(bot.options);
    bot._client = client;
    bot.emit("init_client", client);
    bot.bots = options.bots ?? [bot];
    return bot;
  }
}
module.exports = Bot;
