const loadPrismarineChat = require("prismarine-chat");
const KaboomChatParser = require("../chat_parsers/kaboom");
const CreayunChatParser = require("../chat_parsers/creayun");
const convertNbtComponentToJson = require("../util/nbt_parser");

function tryParse(json) {
  try {
    return JSON.parse(json);
  } catch (error) {
    return { text: "" };
  }
}

class chat {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    const options = context.options;
    let ChatMessage;
    bot.on("registry_ready", (registry) => {
      ChatMessage = loadPrismarineChat(registry);
    });
    if (options.mode === "savageFriends") {
      bot.chatParsers = [CreayunChatParser];
    } else {
      bot.chatParsers = [KaboomChatParser];
    }
    bot.on("packet.profileless_chat", (packet) => {
      let message
      let sender
      if (bot._client.version === "1.21" || bot.options.version === "1.21.1") {
        message = convertNbtComponentToJson(null, packet.message);
        sender = convertNbtComponentToJson(null, packet.name);
      } else {
        message = tryParse(packet.message);
        sender = tryParse(packet.name);
      }

      if (bot.options.version === "1.21" || bot._client.version === "1.21.1") {
        switch (packet.type.registryIndex) {
          case 6:
            bot.emit("message", {
              translate: "chat.type.announcement",
              with: [sender, message],
            });
            break;
          case 5:
            bot.emit("message", message);
            break;
          case 4:
            bot.emit("message", {
              translate: "commands.message.display.outgoing",
              with: [sender, message],
              color: "gray",
              italic: true,
            });
            break;
          case 3:
            bot.emit("message", {
              translate: "commands.message.display.incoming",
              with: [sender, message],
              color: "gray",
              italic: true,
            });
            break;
          case 2:
            bot.emit("message", {
              translate: "chat.type.emote",
              with: [sender, message],
            });
            break;
        }
      } else {
        switch (packet.type) {
          case 1:
            bot.emit("message", {
              translate: "chat.type.emote",
              with: [sender, message],
            });
            break;
          case 2:
            bot.emit("message", {
              translate: "commands.message.display.incoming",
              with: [sender, message],
              color: "gray",
              italic: true,
            });
            break;
          case 3:
            bot.emit("message", [
              {
                translate: "commands.message.display.outgoing",
                with: [sender, message],
                color: "gray",
                italic: true,
              },
            ]);
            break;
          case 4:
            bot.emit("message", [message]);
            break;
          case 5:
            bot.emit("message", [
              { translate: "chat.type.announcement", with: [sender, message] },
            ]);
            break;
        }
      }
    });

    bot.on("packet.player_chat", (packet, data) => {
      let unsigned;

      if (bot.options.version === "1.21" || bot.options.version === "1.21.1") {
        unsigned = convertNbtComponentToJson(null, packet.unsignedChatContent);
      } else {
        unsigned = tryParse(packet.unsignedChatContent);
      }

      if (bot.options.version === "1.21" || bot.options.version === "1.21.1") {
        switch (packet.type.registryIndex) {
          case 6:
            bot.emit("message", {
              translate: "chat.type.announcement",
              with: [
                bot.players.find((player) => player.uuid === packet.senderUuid)
                  .profile.name,
                packet.plainMessage,
              ],
            });
            break;
          case 5:
            bot.emit("message", unsigned);
            break;
          case 4:
            bot.emit("message", {
              translate: "commands.message.display.outgoing",
              with: [
                bot.players.find((player) => player.uuid === packet.senderUuid)
                  .profile.name,
                packet.plainMessage,
              ],
              color: "gray",
              italic: true,
            });
            break;
          case 3:
            bot.emit("message", {
              translate: "commands.message.display.incoming",
              with: [
                bot.players.find((player) => player.uuid === packet.senderUuid)
                  .profile.name,
                packet.plainMessage,
              ],
              color: "gray",
              italic: true,
            });
            break;
          case 2:
            bot.emit("message", {
              translate: "chat.type.emote",
              with: [
                bot.players.find((player) => player.uuid === packet.senderUuid)
                  .profile.name,
                packet.plainMessage,
              ],
            });
            break;
        }
      } else {
        switch (packet.type) {
          case 5:
            bot.emit("message", {
              translate: "chat.type.announcement",
              with: [
                bot.players.find((player) => player.uuid === packet.senderUuid)
                  .profile.name,
                packet.plainMessage,
              ],
            });
            break;
          case 4:
            bot.emit("message", unsigned);
            break;
          case 3:
            bot.emit("message", {
              translate: "commands.message.display.outgoing",
              with: [
                bot.players.find((player) => player.uuid === packet.senderUuid)
                  .profile.name,
                packet.plainMessage,
              ],
              color: "gray",
              italic: true,
            });
            break;
          case 2:
            bot.emit("message", {
              translate: "commands.message.display.incoming",
              with: [
                bot.players.find((player) => player.uuid === packet.senderUuid)
                  .profile.name,
                packet.plainMessage,
              ],
              color: "gray",
              italic: true,
            });
            break;
          case 1:
            bot.emit("message", {
              translate: "chat.type.emote",
              with: [
                bot.players.find((player) => player.uuid === packet.senderUuid)
                  .profile.name,
                packet.plainMessage,
              ],
            });
            break;
        }
      }

      tryParsingMessage(unsigned, {
        senderUuid: packet.senderUuid,
        players: bot.players,
        getMessageAsPrismarine: bot.getMessageAsPrismarine,
      });
    });

    bot.on("packet.system_chat", (packet) => {
      let message;

      if (bot.options.version === "1.21" || bot.options.version === "1.21.1") {
        message = convertNbtComponentToJson(null, packet.content);
      } else {
        message = tryParse(packet.content);
      }

      
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

      bot.emit("message", message);

      tryParsingMessage(message, {
        players: bot.players,
        getMessageAsPrismarine: bot.getMessageAsPrismarine,
      });
    });

    function tryParsingMessage(message, data) {
      let parsed;

      for (const parser of bot.chatParsers) {
        parsed = parser(message, data);
        if (parsed) break;
      }

      if (!parsed) return;
      bot.emit("parsed_message", parsed);
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
      bot.core.run(`minecraft:tellraw ${selector} ` + JSON.stringify(message));
    };
  }
}
module.exports = chat;
