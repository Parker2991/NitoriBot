const mc = require('minecraft-protocol');
const { EventEmitter } = require('events');
EventEmitter.defaultMaxListeners = 5e6;
const util = require('util');
const ChatMessage = require('prismarine-chat');
function createBot(context) {
  const bot = new EventEmitter();
  const config = context.config;
  bot.server = {
  // Set some default values in options
    host: config.server.host ??= 'localhost',
    username: config.server.username ??= 'Player',
    hideErrors: config.server.hideErrors ??= true, // HACK: Hide errors by default as a lazy fix to console being spammed with them
  };

  bot.server = config.server;
  // Create our client object, put it on the bot, and register some events
  bot.on('init_client', client => {
    client.on('packet', (data, meta) => {
      bot.emit('packet', data, meta)
      bot.emit('packet.' + meta.name, data);
    })

    client.on('login', (data) => {
      bot.uuid = client.uuid
      bot.username = client.username
    })

    client.on('disconnect', data => {
      bot.emit("disconnect", data);
    })

    client.on('end', reason => {
      bot.emit('end', reason);
    })

    client.on('error', error => {
      endCount++
      bot.emit('error', error);
    })

    client.on("keep_alive", ({ keepAliveId }) => {
      bot.emit("keep_alive", { keepAliveId })
    })

    client.on('kick_disconnect', (data) => {
      bot.emit("kick_disconnect", data.reason)
    })

    process.on("uncaughtException", (e) => {
//      console?.warn(e.stack)
    });
  })

  const client = mc.createClient(bot.server)
  bot._client = client
  bot.emit('init_client', client)
//  bot.bots = options.bots ?? [bot]
  return bot;
}
module.exports = createBot;
