const channels = "extras:register\0extras:unregister\0extras:message\0";
import { Bot } from "../../Bot";

export class Extras {
  public static channels = [];

  public register (channel: any) {

  }

  public unregister (channel: any) {

  }

  public unregisterAll () {

  }

  public send (channel: any, ...message: any) {
    const bot = Bot;
    const buffer = Buffer.from(channel, "utf8");

    buffer[buffer.length - 1]! |= 0x80;
  
    bot._client.write("custom_payload", {
      channel: "extras:message",
      data: Buffer.concat([buffer, Buffer.from(`${message}`, "utf8")])
    })
  }


  constructor () {
    const bot = Bot
    bot.listener.on('packet.login', () => {
      bot._client.write("custom_payload", {
        channel: "minecraft:register",
        data: Buffer.from(channels, "utf8")
      });
    });
  }
}