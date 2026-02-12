import mc from "minecraft-protocol";
import { Bot } from '../Bot';

export class Reconnect {
  constructor() {
    const bot = Bot
    let client;
    const options = bot.options
    bot.reconnectDelay = options.reconnectDelay;

    bot.listener.on("end", () => {
      setTimeout(() => {
        client = options.client ?? mc.createClient(options)
        bot._client = client;
        bot.listener.emit("init_client", bot._client);
      }, bot.reconnectDelay);
    });
  }
}