import mc from "minecraft-protocol";
import { EventEmitter } from "events";
import { Chat } from "./modules/Chat";
import { Extras } from "./modules/extrasMessaging/Extras";
import { IRC } from "./modules/IRC";
import { Position } from "./modules/Position";
import { CommandCore } from "./modules/CommandCore"
import { Tellraw } from "./modules/Tellraw";
import { Reconnect } from "./modules/Reconnect";

//export const listener = new EventEmitter();

//export let reconnect: any;

//export let chat: any

export class Bot {
  public static readonly listener = new EventEmitter()

  public static reconnectDelay: any;

  public static reconnect: Reconnect

  public static tellraw: any;

  public static core: CommandCore;

  public static ircClient: any;

  public static irc: IRC;

  public static _client: any;

  public static chat: Chat;

  public static extras: Extras;

  public static bots: Array<any>;

  public static options: any;

  public static entityId: any;

  public static config: any;

  public static position: Position;

  private static loadModules () {
    this.chat = new Chat();
    this.extras = new Extras();
    this.irc = new IRC();
    this.position = new Position();
    this.core = new CommandCore();
    this.tellraw = new Tellraw().tellraw
    this.reconnect = new Reconnect();
  }

  private static clearRegistry () {

  }

  private static loadRegistry () {

  }

  public static Bot (options: any, config: any, ircClient: any) {
    const listener = this.listener;

    this.options = {
      host: options.host ??= "localhost",
      port: options.port ??= 25565,
      version: options.version ??= "1.21.8",
      username: options.username ??= "FNFBoyfriendBot"
    };

    this.config = {
      prefixes: config.prefixes ??= ["!"],
      console: {
        prefix: config.console.prefix ??= "c.",
        timezone: "America/CHICAGO",
      },
      bots: config.bots ??= []
    }

    this.options = options;

    this.config = config;

    this.ircClient = ircClient;
    
    listener.on("init_client", (client: any) => {
      client.on('packet', (data: any, meta: any) => {
        listener.emit("packet", data, meta);
        listener.emit("packet." + meta.name, data)
      });

      client.on('login', (packet: any) => {
        this.entityId = packet.entityId;
      });

      client.on('error', (error: any) => {})

      client.on("end", (reason: any) => {
        listener.emit("end", reason);
      });
    });

    const client = mc.createClient(options);

    listener.emit('init_client', client);
    this.bots = options.bots ?? [listener];
    this._client = client;

    this.loadModules();

    return Bot;
  }
}
