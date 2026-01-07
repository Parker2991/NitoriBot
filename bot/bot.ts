import mc from "minecraft-protocol";
import FNFBoyfriendBot from "./FNFBoyfriendBot";

export class createBot extends FNFBoyfriendBot {
  constructor (options: any, config: any) {
    super(options, config);
    const bot = this;
    
    bot.on("init_client", (client: any) => {
      client?.on('packet', (data: any, meta: any) => {
        bot.emit("packet", data, meta);
        bot.emit("packet." + meta.name, data)
      });

      client.on('login', (packet: any) => {
        bot.entityId = packet.entityId;
      });

      client?.on("end", (reason: any) => {
        bot.emit("end", reason);
      });

    //  client.on("error", (error: any) => console.error(error))
    })

    const client = mc.createClient(options)

    bot._client = client;
    bot.emit('init_client', client);
    bot.bots = options.bots ?? [bot]
    return bot;
  }
}