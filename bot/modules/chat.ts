import convertNbtComponentToJson from "../util/convertNbtComponentToJson";
import loadPrismarineChat from 'prismarine-chat';
import kaboom from '../chatParsers/kaboom';
import chipmunkmod from '../chatParsers/chipmunkmod';

// taken from FNFBoyfriendBot v8.0.0

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

export class chat {
  constructor (context: any) {
    const bot = context.bot;
    const config = context.config;
    const options = bot.options;
    let ChatMessage: any;
    let chatTypes: any;
    bot.on("registry_ready", (registry: any) => {
      ChatMessage = loadPrismarineChat(registry);
    });

    bot.on('packet.registry_data', (packet: any) => {
      if (packet.id !== 'minecraft:chat_type') return // taken from the 1.21.1 build of my bot that chayapak made
      chatTypes = packet.entries
    })

    bot.chatParsers = [kaboom, chipmunkmod];
    
  /*  if (options.mode === "savageFriends") {
      bot.chatParsers = [creayun];
    } else {
      bot.chatParsers = [
        kaboom,
        creayun,
        chipmunkmod,
        vanilla, 
        me
      ];
    }*/

    bot.on("packet.profileless_chat", (packet: any) => {
      const sender = convertNbtComponentToJson(null, packet.name);
      const message = convertNbtComponentToJson(null, packet.message);
      const target = convertNbtComponentToJson(null, packet.target)
      const type = packet.type.chatType;
      const parsed = parseChatType(chatTypes, type, sender, target, message)
      bot.emit('message', {
        type: "minecraft:disguised_chat",
        message: parsed
      })

      if (bot.debugEnabled) bot.emit("disguised", parsed);

      parseMessage(parsed, { senderName: sender, players: bot.players, getMessageAsPrismarine: bot.getMessageAsPrismarine, chatType: "minecraft:diguised_chat" })
    });

    bot.on("packet.player_chat", (packet: any) => {
      const unsigned = convertNbtComponentToJson(null, packet.unsignedChatContent);
      const networkName = convertNbtComponentToJson(null, packet.networkName)
      bot.emit('message', {
        type: "minecraft:player_chat",
        message: unsigned
      })

      parseMessage(unsigned, {
        senderUuid: packet.senderUuid,
        players: bot.players,
        getMessageAsPrismarine: bot.getMessageAsPrismarine,
        chatType: "minecraft:player_chat"
      });

      if (bot.debugEnabled) bot.emit("player", unsigned)
    });

    bot.on("packet.system_chat", (packet: any) => {
      const message = convertNbtComponentToJson(null, packet.content);
      bot.emit("system", message);
      if (
      //  message.translate === "advMode.setCommand.success"
        //||
        message.translate === "multiplayer.message_not_delivered"
        ||
        packet.isActionBar
        ||
        message.translate === "advMode.notAllowed"
      )
        return;

      bot.emit("message", {
        type: "minecraft:system_chat",
        message: message
      });

     // if (bot.debugEnabled) bot.emit("system", message)

      parseMessage(message, {
        players: bot.players,
        getMessageAsPrismarine: bot.getMessageAsPrismarine,
        chatType: "minecraft:system_chat",
      });
    });

    function parseMessage(message: any, data: any) {
      try {
        let parsed;

        for (const parser of bot.chatParsers) {
          parsed = parser(message, data);
          if (parsed) break;
        }

        if (!parsed) return;
        
        bot.emit("parsed_message", parsed);
      } catch (e: unknown) {
        console.log(e);
      }
    }

    bot.getMessageAsPrismarine = (message: string) => {
      try {
        if (ChatMessage !== undefined) {
          return new ChatMessage(message);
        }
      } catch {}

      return undefined;
    };

    bot.chat = {
      message(message: any) {
        const acc = 0;
        const bitset = Buffer.allocUnsafe(3);
        bitset[0] = acc & 0xff;
        bitset[1] = (acc >> 8) & 0xff;
        bitset[2] = (acc >> 16) & 0xff;
        bot._client.write("chat_message", {
          message: message?.substring(0, 256)?.replaceAll("§", "&"),
          timestamp: BigInt(Date.now()),
          salt: 0n,
          offset: 0,
          acknowledged: bitset,
        });
      },

      command(command: any) {
        bot._client.write("chat_command", {
          command: command?.substring(0, 256)?.replaceAll("§", "&"),
          timestamp: BigInt(Date.now()),
          salt: 0n,
          argumentSignatures: [],
          signedPreview: false,
          messageCount: 0,
          acknowledged: Buffer.alloc(3),
          previousMessages: [],
        });
      },

      send(message: any) {
        if (message.startsWith("/")) {
          bot.chat.command(message.substring(1));
          return;
        }
        bot.chat.message(message);
      }
    }
  }
}