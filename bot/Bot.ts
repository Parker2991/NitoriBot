import mc from "minecraft-protocol";
import { EventEmitter } from "events";
import { Chat } from "./modules/Chat";
import fs from "fs";
import path from "path";
import java from "java";
java.classpath.push("nbt-3.2.1.jar");
console.log(java)
/*net.lenni0451.mcstructs
java.classpath.push("commons-lang3-3.19.0.jar");
itemRegistry.getMeta(123)
    .types(ItemType.HELMET)
    .tags(ItemTag.DAMAGEABLE, ItemTag.MATERIAL_LEATHER)
    .maxCount(1)
    .maxDamage(321);
*/
export class Bot {
  public static listener = new EventEmitter();

  public static chat: any;

  public static bots: Array<any>

  public static options: any;

  public static entityId: any;

  public static config: any;

  private static loadModules () {
    new Chat(this)
  }

  public static Bot (options: any, config: any) {
    const listeners = Bot.listener;

    Bot.options = {
      host: options.host ??= "localhost",
      port: options.port ??= 25565,
      version: options.version ??= "1.21.8",
      username: options.username ??= "FNFBoyfriendBot"
    };

    Bot.config = {
      prefixes: config.prefixes ??= ["!"],
      console: {
        prefix: config.console.prefix ??= "c.",
        timezone: "America/CHICAGO",
      },
      bots: config.bots ??= []
    }

    Bot.options = options;

    Bot.config = config;

    
    listeners.on("init_client", (packet: any) => {
      client?.on('packet', (data: any, meta: any) => {
        listeners.emit("packet", data, meta);
        listeners.emit("packet." + meta.name, data)
      });

      client.on('login', (packet: any) => {
        Bot.entityId = packet.entityId;
      });

      client?.on("end", (reason: any) => {
        listeners.emit("end", reason);
      });
    });

    const client = mc.createClient(options);

    listeners.emit('init_client', client);
    Bot.bots = options.bots ?? [listeners];

    this.loadModules()
    return Bot;
  }
}
