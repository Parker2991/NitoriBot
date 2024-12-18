
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

    bot.on('packet.player_chat', (data) => {
      const unsigned = data.unsignedChatContent;
      try {
      switch (data.type) {
        case 1:

        break;
        case 3:
          bot.emit('playerChat', [
            {
              translate: "[%s %s %s%s %s] ",
              color: "dark_gray",
              with: [
                { text: 'Player Chat', color: 'dark_purple' },
                { text: '|' },
                { text: 'Packet Type', color: "aqua" },
                { text: ':' },
                { text: data.type, color: 'gold' },
              ],
            },
            { translate: "commands.message.display.outgoing",
              with: [
                JSON.parse(data.networkName).insertion,
                data.plainMessage
              ],
              color: "gray",
              italic: true
            }
          ])
      break
      case 2:
        bot.emit('playerChat', [
          {
            translate: "[%s %s %s%s %s] ",
            color: "dark_gray",
            with: [
              { text: 'Player Chat', color: 'dark_purple' },
              { text: '|' },
              { text: 'Packet Type', color: "aqua" },
              { text: ':' },
              { text: data.type, color: 'gold' },
            ],
          },
          { translate: "commands.message.display.incoming",
             with: [
               JSON.parse(data.networkName).insertion,
               data.plainMessage
             ],
             color: "gray",
             italic: true
          }
        ])
      break
        case 4:
          bot.emit('playerChat', [
            {
              translate: "[%s %s %s%s %s] ",
              color: "dark_gray",
              with: [
                { text: 'Player Chat', color: 'dark_purple' },
                { text: '|' },
                { text: 'Packet Type', color: "aqua" },
                { text: ':' },
                { text: data.type, color: 'gold' },
              ],
            },
            JSON.parse(unsigned)
          ])
        break;
        case 5:
          bot.emit('playerChat', [
            {
              translate: "[%s %s %s%s %s] ",
              color: "dark_gray",
              with: [
                { text: 'Player Chat', color: 'dark_purple' },
                { text: '|' },
                { text: 'Packet Type', color: "aqua" },
                { text: ':' },
                { text: data.type, color: 'gold' },
              ],
            },
            { translate: "chat.type.announcement",
              with: [
                JSON.parse(data.networkName).insertion,
                data.plainMessage
              ]
            }
          ]);
        break;
        default:
          console.warn('invalid chat type');
      }
      } catch (e) {
        console.log(e.stack);
      }
    })

    bot.on('packet.profileless_chat', (packet) => {
      const message = JSON.parse(packet.message)
      const sender = JSON.parse(packet.name)
      switch (packet.type) {
        case 1:
          bot?.emit('profilelessChat', [
            {
              translate: "[%s %s %s%s %s] ",
              color: "dark_gray",
              with: [
                { text: 'Profileless Chat', color: 'dark_aqua' },
                { text: '|' },
                { text: 'Packet Type', color: "aqua" },
                { text: ':' },
                { text: packet.type, color: 'gold' },
              ],
            },
            { translate: "chat.type.emote",
               with: [
                 sender,
                 message
               ]
            }
          ])
        break
        case 2:
          bot.emit('profilelessChat', [
            {
              translate: "[%s %s %s%s %s] ",
              color: "dark_gray",
              with: [
                { text: 'Profileless Chat', color: 'dark_aqua' },
                { text: '|' },
                { text: 'Packet Type', color: "aqua" },
                { text: ':' },
                { text: packet.type, color: 'gold' },
              ],
            },
            { translate: "commands.message.display.incoming",
              with: [
                sender,
                message
              ],
              color: "gray",
              italic: true
            }
          ])
        break
        case 3:
          bot.emit('profilelessChat', [
            {
              translate: "[%s %s %s%s %s] ",
              color: "dark_gray",
              with: [
                { text: 'Profileless Chat', color: 'dark_aqua' },
                { text: '|' },
                { text: 'Packet Type', color: "aqua" },
                { text: ':' },
                { text: packet.type, color: 'gold' },
              ],
            },
            { translate: "commands.message.display.outgoing",
              with: [
                sender,
                message
              ],
              color: "gray",
              italic: true
            }
          ])
        break
        case 4:
          bot.emit('profilelessChat', [
            {
              translate: "[%s %s %s%s %s] ",
              color: "dark_gray",
              with: [
                { text: 'Profileless Chat', color: 'dark_aqua' },
                { text: '|' },
                { text: 'Packet Type', color: "aqua" },
                { text: ':' },
                { text: packet.type, color: 'gold' },
              ],
            },
            message
          ])
        break
        case 5:
          bot.emit('profilelessChat', [
            {
              translate: "[%s %s %s%s %s] ",
              color: "dark_gray",
              with: [
                { text: 'Profileless Chat', color: 'dark_aqua' },
                { text: '|' },
                { text: 'Packet Type', color: "aqua" },
                { text: ':' },
                { text: packet.type, color: 'gold' },
              ],
            },
            { translate: 'chat.type.announcement',
               with: [
                 sender,
                 message
               ]
             }
          ])
        break
      }
    });

    bot.on('packet.system_chat', packet => {
      const message = JSON.parse(packet.content)

      if (message.translate === 'advMode.setCommand.success') return // Ignores command set message

      if (message.translate === 'multiplayer.message_not_delivered') return;

      bot.emit('system_chat', { message, actionbar: packet.isActionBar })

      if (packet.isActionBar) {
        return
      }

      bot.emit('systemChat', [
        {
          translate: "[%s] ",
          color: "dark_gray",
          with: [
            { text: 'System Chat', color: 'dark_blue' },
          ],
        },
        message
      ]);

      bot.emit('system_chat', message); // this is for selfcare
    });

/*    bot.chat = {
      send (args) {
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
      },

      tellraw (selector, message) {
        bot.core.run(`minecraft:tellraw ${selector} ${JSON.stringify(message)}`);
      }
    }*/
  }
}
