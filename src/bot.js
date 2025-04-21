const mc = require('minecraft-protocol');
const { EventEmitter } = require('events');
const ChatMessage = require("prismarine-chat");
EventEmitter.defaultMaxListeners = Infinity;
let disconnectCount = 0;

function createBot(options = {}, config) {
  const bot = new EventEmitter();
  bot.options = {
  // Set some default values in options
    host: options.host ??= 'localhost',
    username: options.username ??= 'Player',
    hideErrors: options.hideErrors ??= true, // HACK: Hide errors by default as a lazy fix to console being spammed with them
  };
  bot.options = options;
  // Create our client object, put it on the bot, and register some events
  bot.on('init_client', client => {
    client.on('packet', (data, meta) => {
      bot.emit('packet', data, meta)
      bot.emit('packet.' + meta.name, data);
    })

    client.on('login', (data) => {
      bot.uuid = client.uuid;
      bot.username = client.username;
    })

    client.on("disconnect", (data) => {
      bot.console.warn(`${ChatMessage(bot._client.version).fromNotch("§8[§bClient Reconnect§8]§r")?.toAnsi()} ${ChatMessage(bot._client.version).fromNotch(data.reason)?.toAnsi()}`);
      bot?.discord?.channel?.send(ChatMessage(bot._client.version).fromNotch(data.reason)?.toString());
    });

    client.on("end", (reason) => {
      bot.emit("end", reason);
      disconnectCount++;
    });

    client.on("error", (error) => {
      try {
        if (disconnectCount === 10) {
          bot.console.info("stopped logging disconnect messages for now...");
          bot?.discord?.channel?.send("stopped logging disconnect messages for now...");
          return;
        } else if (disconnectCount > 10) {
          return;
        } else {
          bot.console.warn(ChatMessage(bot._client.version).fromNotch(`§8[§bClient Reconnect§8]§r ${error.toString()}`)?.toAnsi());
          bot?.discord?.channel?.send(error.toString());
        }
      } catch (e) {
        console.log(e.stack);
      }
    });

    client.on("kick_disconnect", (data) => {
      bot.console.warn(`${ChatMessage(bot._client.version).fromNotch("§8[§bClient Reconnect§8]§r")?.toAnsi()} ${ChatMessage(bot._client.version).fromNotch(data.reason)?.toAnsi()}`);
      bot?.discord?.channel?.send(ChatMessage(bot._client.version).fromNotch(data.reason)?.toString());
    });

    process.on("uncaughtException", (e) => {
    });

    client.on("success", () => {
      disconnectCount = 0;
    });
  })

  const client = options.client ?? new mc.createClient(bot.options)
  bot._client = client
  bot.emit('init_client', client)
  bot.bots = options.bots ?? [bot]
  return bot
}
module.exports = createBot;
