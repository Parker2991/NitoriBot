const convertNbtComponentToJson = require('../util/nbt_parser')
/*
  please note that the code here for 1.21.8 
  was ported from the FNFBoyfriendBot Reignite 1.21.8 port
  and had SkiBot's console format added to it
*/

function parse (chatTypes, type, sender, target, message) {
  const chatType = chatTypes[type]
  const parsed = convertNbtComponentToJson(null, chatType.value.value.chat)
  const json = { translate: parsed.translation_key, with: [] }

  for (const parameter of parsed.parameters) {

    switch (parameter) {
      case "sender": json.with.push(sender)
      break;
      case "target": json.with.push(target)
      case "content": json.with.push(message)
      break;
    }
  }

  return json
}

module.exports = {
  data: {
    description: "parses chat",
    enabled: true,
    name: "chat",
    type: "logging"
  },
  inject (context) {
    const bot = context.bot;
    const config = context.config;

    bot.on('packet.registry_data', (packet) => {
      if (packet.id !== 'minecraft:chat_type') return // taken from FNFBoyfriendBot Reignite 1.21.8 port
      chatTypes = packet.entries
    })

    bot.on('packet.player_chat', (packet) => {
      const unsigned = convertNbtComponentToJson(null, packet.unsignedChatContent);
      bot.emit('message', [
        {
          translate: "[%s %s %s%s %s] ",
          color: "dark_gray",
          with: [
            { text: 'Player Chat', color: 'dark_purple' },
            { text: '|' },
            { text: 'Packet Type', color: "aqua" },
            { text: ':' },
            { text: packet.type.chatType, color: 'gold' },
          ],
        },
        unsigned
      ])
    });

    bot.on('packet.profileless_chat', (packet) => {
      const sender = convertNbtComponentToJson(null, packet.name);
      const message = convertNbtComponentToJson(null, packet.message);
      const target = convertNbtComponentToJson(null, packet.target)
      const type = packet.type.chatType;
      const parsed = parse(chatTypes, type, sender, target, message)

      bot.emit('message', [
        {
          translate: "[%s %s %s%s %s] ",
          color: "dark_gray",
          with: [
            { text: 'Profileless Chat', color: 'dark_aqua' },
            { text: '|' },
            { text: 'Packet Type', color: "aqua" },
            { text: ':' },
            { text: packet.type.chatType, color: 'gold' },
          ],
        },
        parsed
      ])
    });

    bot.on('packet.system_chat', packet => {
      try {
      const message = convertNbtComponentToJson(null, packet.content)

      if (message.translate === 'advMode.setCommand.success') return // Ignores command set message

      if (message.translate === 'multiplayer.message_not_delivered') return;

      if (packet.isActionBar) {
        return
      }

      bot.emit('message', [
        {
          translate: "[%s] ",
          color: "dark_gray",
          with: [
            { text: 'System Chat', color: 'dark_blue' },
          ],
        },
        message
      ]);

      bot.emit('system_chat_selfcare', message); // this is for selfcare
    } catch (e) {
      console.log(e.stack)
    }
    });

    bot.chat = (args) => {
      if (args.startsWith('/')) {
        bot._client.write('chat_command', {
          command: args?.substring(1).substring(0, 256),
          timestamp: BigInt(Date.now()),
          salt: 0n,
          argumentSignatures: [],
          signedPreview: false,
          messageCount: 0,
          acknowledged: Buffer.alloc(3),
          previousMessages: []
        })
        return;
      }
      const acc = 0;
      const bitset = Buffer.allocUnsafe(3);
      bitset[0] = acc & 0xFF;
      bitset[1] = (acc >> 8) & 0xFF;
      bitset[2] = (acc >> 16) & 0xFF;
      bot._client.write('chat_message', {
        message: args?.substring(0, 256),
        timestamp: BigInt(Date.now()),
        salt: 0n,
        offset: 0,
        acknowledged: bitset
      })
    }

    bot.tellraw = (selector, message) => {
      bot.core.run(`minecraft:tellraw ${selector} ${JSON.stringify(message)}`);
    }
  }
}
