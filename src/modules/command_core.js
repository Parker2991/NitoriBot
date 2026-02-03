const mcData = require('minecraft-data')('1.20.2');
const nbt = require('prismarine-nbt');

module.exports = {
  data: {
    description: "runs commands in command blocks",
    enabled: true,
    name: "command core",
    type: "commands"
  },
  inject (context) {
    const bot = context.bot;
    const config = context.config;
    const Item = require("prismarine-item")(bot.server.version);

    bot.core = {
      area: {
        start: config.core?.area.start ?? { x: 0, y: 0, z: 0 },
        end: config.core?.area.end ?? { x: 15, y: 0, z: 15 }
      },

      position: null,

      itemPosition: null,

      currentBlockRelative: { x: 0, y: 0, z: 0 },

      usePlacedCommandBlock: false,

      chatRefill () {
        const pos = bot.core.position
        const { start, end } = bot.core.area

        if (!pos) return
        const command = `/minecraft:fill ${pos.x + start.x} ${pos.y + start.y} ${pos.z + start.z} ${pos.x + end.x} ${pos.y + end.y} ${pos.z + end.z} repeating_command_block{CustomName:${JSON.stringify(config.core.name)}}`
        bot.chat(`${command}`)
      },

      itemRefill () {
        const pos = bot.core.position;
        const { start, end } = bot.core.area;
        const itemPosition = bot.core.itemPosition;

        if (!pos) return;

        const command = `minecraft:fill ${pos.x + start.x} ${pos.y + start.y} ${pos.z + start.z} ${pos.x + end.x} ${pos.y + end.y} ${pos.z + end.z} command_block{CustomName:${JSON.stringify(config.core.name)}}`

        bot._client.write('set_creative_slot', {
            slot: 36,
            item: Item.toNotch(
              new Item(
                bot.registry.itemsByName.repeating_command_block.id,
                1,
                0,
              ),
            ),
        });

        bot._client.write('block_dig', {
          status: 0,
          location: itemPosition,
          face: 0
        });

        bot._client.write('block_place', {
          hand: 0,
          location: itemPosition,
          direction: 0,
          cursorX: 0.1,
          cursorY: 0,
          cursorZ: 0.1,
          insideBlock: false
        });

        if (bot.core.usePlacedCommandBlock) {
          return
        } else {
          bot._client.write('update_command_block', {
            location: itemPosition,
            command,
            flags: 5,
            mode: 1
          })
        }
      },

      move (pos = bot.position) {
        bot.core.position = {
          x: Math.floor(pos.x / 16) * 16,
          y: 0,
          z: Math.floor(pos.z / 16) * 16
        };

        bot.core.itemPosition = {
          x: pos.x,
          y: pos.y -1,
          z: pos.z
        }

        if (config.core.itemRefill === true) {
          bot.core.itemRefill();
        } else {
          bot.core.chatRefill();
        }
      },

      currentBlock () {
        const relativePosition = bot.core.currentBlockRelative
        const corePosition = bot.core.position
        if (!corePosition) return null
        return { x: relativePosition.x + corePosition.x, y: relativePosition.y + corePosition.y, z: relativePosition.z + corePosition.z }
      },

      incrementCurrentBlock () {
        const relativePosition = bot.core.currentBlockRelative
        const { start, end } = bot.core.area

        relativePosition.x++

        if (relativePosition.x > end.x) {
          relativePosition.x = start.x
          relativePosition.z++
        }

        if (relativePosition.z > end.z) {
          relativePosition.z = start.z
          relativePosition.y++
        }

        if (relativePosition.y > end.y) {
          relativePosition.x = start.x
          relativePosition.y = start.y
          relativePosition.z = start.z
        }
      },

      run (command) {
        const location = bot.core.currentBlock();
        const itemPosition = bot.core.itemPosition;

        if (!location || bot.server.mode === "savageFriends") return;
        if (bot.core.usePlacedCommandBlock) {
          bot._client.write('update_command_block', {
            command: command.substring(0, 32767),
            location: itemPosition,
            mode: 1,
            flags: 5,
          });

          bot.core.incrementCurrentBlock();
        } else {
          bot._client.write('update_command_block', {
            command: command.substring(0, 32767),
            location,
            mode: 1,
            flags: 5
          });

          bot.core.incrementCurrentBlock();
        }
      },
    }

    bot.on('move', () => {
      if (bot.server.mode === "savageFriends") return;
      bot.core.move(bot.position)
    })

    bot.on('packet.block_change', (data) => {

    })
  }
}
