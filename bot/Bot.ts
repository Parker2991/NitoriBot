import mc from "minecraft-protocol";
import { EventEmitter } from "events";
export class Bot {
  public listener = new EventEmitter();

  public chat: any;

  public bots: Array<any>

  public options: any;

  public entityId: any;

  constructor (options: any, config: any) {
    const listener = this.listener;

    this.options = {
      host: options.host ??= "localhost",
      port: options.port ??= 25565,
      version: options.version ??= "1.21.8",
      username: options.username ??= "FNFBoyfriendBot"
    };

    this.options = options;

    listener.on("init_client", (packet: any) => {
      client?.on('packet', (data: any, meta: any) => {
        listener.emit("packet", data, meta);
        listener.emit("packet." + meta.name, data)
      });

      client.on('login', (packet: any) => {
        this.entityId = packet.entityId;
      });

      client?.on("end", (reason: any) => {
        listener.emit("end", reason);
      });
    });

    const client = mc.createClient(options);

    listener.emit('init_client', client);
    this.bots = options.bots ?? [listener];

    return this;
  }  
}