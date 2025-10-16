const loadPrismarineChat = require("prismarine-chat");
const convertNbtComponentToJson = require("../util/convertNbtComponentToJson");
const kaboom = require("../chatParsers/kaboom");
const creayun = require("../chatParsers/creayun");
const chipmunkmod = require("../chatParsers/chipmunkmod");
const vanilla = require('../chatParsers/vanilla/vanilla');
const me = require('../chatParsers/vanilla/me')
const { stringify } = require('../util/json');

function parseNbt (chatTypes, type, sender, target, message) {
  const chatType = chatTypes[type]
  const parsed = convertNbtComponentToJson(null, chatType.value.value.chat)
  let json = { translate: parsed.translation_key, with: [] }

  for (const parameter of parsed.parameters) {

    switch (parameter) {
      case "sender": json.with.push(sender)
      break;
      case "target": json.with.push(target)
      case "content": json.with.push(message)
      break;
    }
  }

  if (type === 4) json = message

  return json
}

class chat {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    const options = context.options;
    let ChatMessage;
    let chatTypes;
    bot.on("registry_ready", (registry) => {
      ChatMessage = loadPrismarineChat(registry);
    });

    bot.on('packet.registry_data', (packet) => {
      if (packet.id !== 'minecraft:chat_type') return // taken from the 1.21.1 build of my bot that chayapak made
      chatTypes = packet.entries

    })

    if (options.mode === "savageFriends") {
      bot.chatParsers = [creayun];
    } else {
      bot.chatParsers = [
        kaboom,
        creayun,
        chipmunkmod,
        vanilla, 
        me
/*        KaboomChatParser,
        ChipmunkModChatParser,
        VanilaChatParser,*/
      ];
    }

    bot.on("packet.profileless_chat", (packet) => {
      const sender = convertNbtComponentToJson(null, packet.name);
      const message = convertNbtComponentToJson(null, packet.message);
      const target = convertNbtComponentToJson(null, packet.target)
      const type = packet.type.chatType;
      const parsed = parseNbt(chatTypes, type, sender, target, message)
      bot.emit('message', {
        type: "minecraft:disguised_chat",
        message: parsed
      })

      parseMessage(parsed, { senderName: sender, players: bot.players, getMessageAsPrismarine: bot.getMessageAsPrismarine, chatType: "minecraft:diguised_chat" })
    });

    bot.on("packet.player_chat", (packet) => {
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
    });

    bot.on("packet.system_chat", (packet) => {
      const message = convertNbtComponentToJson(null, packet.content);
      if (
        message.translate === "advMode.setCommand.success" &&
        config?.debug?.commandSetMessage === false
      )
        return;
      if (message.translate === "multiplayer.message_not_delivered") return;

      if (packet.isActionBar) {
        return;
      }

      if (message.translate === "advMode.notAllowed") return;

      bot.emit("message", {
        type: "minecraft:system_chat",
        message: message
      });
      bot.emit("system_chat", message);

      parseMessage(message, {
        players: bot.players,
        getMessageAsPrismarine: bot.getMessageAsPrismarine,
        chatType: "minecraft:system_chat",
      });
    });

    function parseMessage(message, data) {
      try {
        let parsed;

        for (const parser of bot.chatParsers) {
          parsed = parser(message, data);
          if (parsed) break;
        }

        if (!parsed) return;
        bot.emit("parsed_message", parsed);
      } catch (e) {
        console.log(e.stack);
      }
    }

    bot.getMessageAsPrismarine = (message) => {
      try {
        if (ChatMessage !== undefined) {
          return new ChatMessage(message);
        }
      } catch {}

      return undefined;
    };

    bot.chat = {
      message(message) {
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

      command(command) {
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

      send(message) {
        if (message.startsWith("/")) {
          bot.chat.command(message.substring(1));
          return;
        }
        bot.chat.message(message);
      },
    };

    bot.tellraw = (selector, message) => {
      bot.core.run(`minecraft:tellraw ${selector} ` + stringify(message));
    };
  }
}
module.exports = chat;
