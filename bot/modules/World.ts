const convertNbtComponentToJson = require('../util/ConvertNbtComponentToJson');

export default class world {
  constructor (context: any) {
    const bot = context.bot;
    const config = context.config;
    const options = bot.options;
    bot.world = {
      dimensions: [],
      chunkColumns: [],
      currentDimension: undefined,
      getBlock (blockStateID: any) {
        const getBlocks = bot.registry.blocksByStateId;

        return getBlocks[blockStateID];
      },
      chunk: undefined,

      getBlockNBT (location: any) {
        const transactionId = Math.floor(Math.random() * 10000);

        bot._client.write('query_block_nbt', { transactionId, location });

        bot.on('packet.nbt_query_response', (packet: any) => {
          try {
            if (packet.transactionId == transactionId) {
              const parsed = convertNbtComponentToJson(null, packet.nbt);
            }
          } catch (e) {
            console.log(e)
          }
        })
      }
    }
    
    bot.on('packet.login', (packet: any) => {
      
    });

    bot.on('packet.registry_data', (packet: any) => {
      let dimension;
      if (packet.id === "minecraft:dimension_type") {
        for (const dimensions of packet.entries) {
          const parseToJson = convertNbtComponentToJson(null, dimensions.value);
          const maxY = parseToJson.height;
          const minY = parseToJson.min_y;

          if (dimensions.key === "minecraft:overworld_caves") dimension = "minecraft:world_flatlands";
          else dimension = dimensions.key
          bot.world.dimensions.push({ name: dimension, minY: minY, maxY: maxY });
        }
      }
    });

    bot.on('packet.map_chunk', (packet: any) => {
      const x = packet.x;
      const z = packet.z;

      bot.world.chunkColumns.push({ x, z });
    });

    bot.on('packet.unload_chunk', (packet: any) => {
      bot.world.chunkColumns = [];
    });

    bot.on('packet.respawn', (packet: any) => {
      bot.world.currentDimension = packet.worldState.name;
    })
  }
}