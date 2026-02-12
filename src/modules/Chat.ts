import { Bot } from "../Bot";
import convertNbtComponentToJson from "../util/ConvertNbtComponentToJson";
let chatTypes = ''

function parseChatType (chatTypes: any, type: any, sender: any, target: any, message: any) {
  const chatType = chatTypes[type]
  const parsed = convertNbtComponentToJson(null, chatType.value.value.chat)
  const array: any[] = []
  for (const parameter of parsed.parameters) {

    switch (parameter) {
      case "sender": array.push(sender)
      break;
      case "target": array.push(target)
      case "content": array.push(message)
      break;
    }
  }
  let json = { translate: parsed.translation_key, with: array }

  if (type === 4) json = message

  return json
}


export class Chat {
  private parseMessage () {

  }

  public static getMessageAsPrismarine () {

  }

  public static chatParsers: any; // stub for now

  public player (packet: any): void {
    const bot = Bot
    const unsigned = convertNbtComponentToJson(null, packet.unsignedChatContent);
    const networkName = convertNbtComponentToJson(null, packet.networkName)
    bot.listener.emit('message', {
      type: "minecraft:player_chat",
      message: unsigned
    })
  }

  private system (packet: any): void {
    const bot = Bot
    const message = convertNbtComponentToJson(null, packet.content);
    if (
      message.translate === "advMode.setCommand.success"
      ||
      message.translate === "multiplayer.message_not_delivered"
      ||
      packet.isActionBar
      ||
      message.translate === "advMode.notAllowed"
    )
    return;

    bot.listener.emit("message", {
      type: "minecraft:system_chat",
      message: message
    });
  }
  
  private diguised (data: any): void {
    const bot = Bot
    const sender = convertNbtComponentToJson(null, data.name);
    const message = convertNbtComponentToJson(null, data.message);
    const target = convertNbtComponentToJson(null, data.target)
    const type = data.type.chatType;
    const parsed = parseChatType(chatTypes, type, sender, target, message)
    bot.listener.emit('message', {
      type: "minecraft:disguised_chat",
      message: parsed
    })
  }
  
  public message (message: any) {
    const bot = Bot
    const acc = 0;
    const bitset = Buffer.allocUnsafe(3);
    bitset[0] = acc & 0xff;
    bitset[1] = (acc >> 8) & 0xff;
    bitset[2] = (acc >> 16) & 0xff;
    bot._client.write('chat_message', {
      message: message?.substring(0, 256)?.replaceAll("§", "&"),
      timestamp: BigInt(Date.now()),
      salt: BigInt(0),
      offset: 0,
      acknowledged: bitset,
    })
  }

  public command (command: any) {
    const bot = Bot;
    bot._client.write("chat_command", {
      command: command?.substring(0, 256)?.replaceAll("§", "&"),
      timestamp: BigInt(Date.now()),
      salt: BigInt(0),
      argumentSignatures: [],
      signedPreview: false,
      messageCount: 0,
      acknowledged: Buffer.alloc(3),
      previousMessages: [],
    });
  }

  public static send () {

  }

  constructor () {
    const bot = Bot;
    const listener = bot.listener;

    listener.on('packet.registry_data', (packet: any) => {
      if (packet.id !== 'minecraft:chat_type') return // taken from the 1.21.1 build of my bot that chayapak made
      chatTypes = packet.entries
    })
  
    listener.on("packet.player_chat", this.player);

    listener.on("packet.system_chat", this.system);

    listener.on("packet.profileless_chat", this.diguised);
  }
}