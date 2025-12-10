import mc from "minecraft-protocol";
import { EventEmitter } from "events";

export class createBot {

  bot: any
  
  bots: any;

  constructor (options: any) {
    this.bot = new EventEmitter();

    const bot = this.bot;

    bot.options = {
      host: (options.host ??= "localhost"),
      username: (options.username ??= "Player"),
      version: (options.version ??= "1.21.8"),
      hideErrors: (options.hideErrors ??= true),
    }
    //options.hideError = true;

    bot.on("init_client", (client: any) => {
      client?.on('packet', (data: any, meta: any) => {
        bot.emit("packet", data, meta);
        bot.emit("packet." + meta.name, data)
      })
    })

    const client = mc.createClient({
      host: options.host ??= 'localhost',
      username: options.username ??= "Player",
      version: options.version ??= "1.21.8"
    })

    bot._client = client;
    bot.emit('init_client', client);
    bot.bots = options.bots ?? [bot]
    return bot;
  }
}
//export = createBot