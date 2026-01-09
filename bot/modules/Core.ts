// i may or may not have used v4's command core for a base here
import { format } from 'util';
//const Chunk = require('prismarine-chunk')
export default class Core {
  constructor (context: any) {
    const bot = context.bot;
    const config = context.config;
    const options = bot.options;
    const Item = require("prismarine-item")(bot.options.version);
 
    bot.core = {
      area: {
        start: options.core?.area.start ?? { x: 0, y: 0, z: 0 },
        end: options.core?.area.end ?? { x: 15, y: 0, z: 15 }
      },
      
      position: null,

      itemPosition: null,

      currentBlockRelative: { x: 0, y: 0, z: 0 },

      refill () {
        const pos = bot.core.position
        const { start, end } = bot.core.area

        if (!pos) return
// {CustomName:${JSON.stringify(config.core.name)}
        bot.chat.command(`fill ${pos.x + start.x} ${pos.y + start.y} ${pos.z + start.z} ${pos.x + end.x} ${pos.y + end.y} ${pos.z + end.z} repeating_command_block`)
      },

      itemRefill () {
        const pos = bot.core.itemPosition;
        //console.log(Item.toNotch(new Item(bot.registry.itemsByName.repeating_command_block.id, 1, 0, 0, 0,)))
       // console.log(Item.toNotch(new Item(bot.registry.itemsByName.repeating_command_block.id)))
        console.log(Item.toNotch(new Item(bot.registry.itemsByName.repeating_command_block.id), 1))
        bot._client.write("set_creative_slot", {
          slot: 36,
          //item: {}
          item: Item.toNotch(
            new Item(
              bot.registry.itemsByName.repeating_command_block.id,
              1,
              0,
            ),
          ),
        });

        bot._client.write("block_dig", {
          status: 0,
          location: pos,
          face: 0,
        });

        bot._client.write("block_place", {
          hand: 0,
          location: pos,
          direction: 0,
          cursorX: 0,
          cursorY: 0,
          cursorZ: 0,
          insideBlock: false,
        });
      },

      useCommandBlock (command: any, location: any, mode: any, flags: any) {
        bot._client.write("update_command_block", {
          command: command?.substring(0, 32767),
          location: location,
          mode: mode,
          flags: flags,
        });
      },

      move (pos = bot.position) {
        bot.core.position = {
          x: Math.floor(pos.x / 16) * 16,
          y: 0,
          z: Math.floor(pos.z / 16) * 16
        }

        bot.core.itemPosition = {
          x: Math.floor(pos.x),
          y: Math.floor(pos.y - 1),
          z: Math.floor(pos.z),
        };

        bot.core.itemRefill();
        //bot.core.refill()
      },

      commandBlockCoords: [],

      needsFilling: false,
      
      cantFill: false,

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

      run (command: any) {
        const location = bot.core.currentBlock()
        if (!location) return

        bot._client.write('update_command_block', { command, location, mode: 1, flags: 0b100 })

        bot.core.incrementCurrentBlock()
      }
    }

    bot.on('move', () => {
      bot.core.move(bot.position)
    });

    bot.on('packet.multi_block_change', (packet: any) => {
      // used some code here from https://code.chipmunk.land/Yaode_owo/js-nmp-1.21.8-chunkparser/src/branch/main/chunkparser.js

      const chunkCoord = packet.chunkCoordinates;

      const findChunkColumn = bot.world.chunkColumns.find((column: any) => column.x === chunkCoord.x && column.z === chunkCoord.z);
      const baseX = chunkCoord.x << 4;
      const baseY = chunkCoord.y << 4;
      const baseZ = chunkCoord.z << 4;

      for (const num of packet.records) {
        const x = baseX | ((num >> 8) & 15);
        const y = baseY | (num & 15);
        const z = baseZ | ((num >> 4) & 15);
        const getBlock = bot.world.getBlock(num >> 12);
        if (getBlock.name === "command_block") {
          bot.core.commandBlockCoords.push({ x: x, y: y, z: z });
        }
        const coreX = bot.core.commandBlockCoords.find((coord: any) => coord.x === x);
        const coreY = bot.core.commandBlockCoords.find((coord: any) => coord.y === y);
        const coreZ = bot.core.commandBlockCoords.find((coord: any) => coord.z === z)
        if (coreX && coreY && coreZ) {
          if (getBlock.name === "command_block" || getBlock.name === "repeating_command_block") return;
          else bot.core.refill()
        }
      }
    });
  }
}
