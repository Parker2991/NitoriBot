const sleep = require("../util/sleep");
const convertNbtComponentToJson = require("../util/convertNbtComponentToJson");

class commandCore {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    const options = context.options;
    const Item = require("prismarine-item")(bot.options.version);
    
    let command;

    bot.core = {
      area: {
        start: config.core?.area.start ?? { x: 0, y: 0, z: 0 },
        end: config.core?.area.end ?? { x: 15, y: 0, z: 15 },
      },

      position: null,

      itemPosition: null,

      currentBlockRelative: { x: 0, y: 0, z: 0 },

      usePlacedCommandBlock: false,

      chatRefill() {
        const pos = bot.core.position;
        const { start, end } = bot.core.area;
        const itemPosition = bot.core.itemPosition;

        if (!pos || !itemPosition) return;

        if (bot.options.mode === "savageFriends") {
          command = `minecraft:fill ${pos.x + start.x} ${pos.y + start.y} ${pos.z + start.z} ${pos.x + end.x} ${pos.y + end.y} ${pos.z + end.z} command_block`;
        } else {
          command = `minecraft:fill ${pos.x + start.x} ${pos.y + start.y} ${pos.z + start.z} ${pos.x + end.x} ${pos.y + end.y} ${pos.z + end.z} command_block{CustomName:${JSON.stringify(config.core.name)}}`;
        }
        bot.chat.command(command);
      },

      itemRefill() {
        const pos = bot.core.position;
        const { start, end } = bot.core.area;
        const itemPosition = bot.core.itemPosition;

        command = `minecraft:fill ${pos.x + start.x} ${pos.y + start.y} ${pos.z + start.z} ${pos.x + end.x} ${pos.y + end.y} ${pos.z + end.z} command_block{CustomName:${JSON.stringify(config.core.name)}}`;
        if (bot.options.mode !== "savageFriends") {
          bot._client.write("set_creative_slot", {
            slot: 36,
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
            location: itemPosition,
            face: 0,
          });

          bot._client.write("block_place", {
            hand: 0,
            location: itemPosition,
            direction: 0,
            cursorX: 0,
            cursorY: 0,
            cursorZ: 0,
            insideBlock: false,
          });

          if (bot.core.usePlacedCommandBlock) {
            return;
          } else {
            bot.core.commandBlock(command, itemPosition, 1, 5);
          }
        }
      },

      move(pos = bot.position) {
        bot.core.position = {
          x: Math.floor(pos.x / 16) * 16,
          y: 0,
          z: Math.floor(pos.z / 16) * 16,
        };

        bot.core.itemPosition = {
          x: Math.floor(pos.x),
          y: Math.floor(pos.y - 1),
          z: Math.floor(pos.z),
        };

        bot.core.commandBlocks = [];

        if (bot.options.itemRefill && bot.options.mode !== "savageFriends") {
          bot.core.itemRefill();
        } else {
          bot.core.chatRefill();
        }
      },

      currentBlock() {
        const relativePosition = bot.core.currentBlockRelative;
        const corePosition = bot.core.position;
        if (!corePosition) return null;
        return {
          x: relativePosition.x + corePosition.x,
          y: relativePosition.y + corePosition.y,
          z: relativePosition.z + corePosition.z,
        };
      },

      incrementCurrentBlock() {
        const relativePosition = bot.core.currentBlockRelative;
        const { start, end } = bot.core.area;

        relativePosition.x++;

        if (relativePosition.x > end.x) {
          relativePosition.x = start.x;
          relativePosition.z++;
        }

        if (relativePosition.z > end.z) {
          relativePosition.z = start.z;
          relativePosition.y++;
        }

        if (relativePosition.y > end.y) {
          relativePosition.x = start.x;
          relativePosition.y = start.y;
          relativePosition.z = start.z;
        }
      },

      commandBlock(command, location, mode, flags) {
        if (!bot.loggedIn) return;
        bot._client.write("update_command_block", {
          command: command?.substring(0, 32767),
          location: location,
          mode: mode,
          flags: flags,
        });
      },

      run(command) {
        const location = bot.core.currentBlock();
        const itemPosition = bot.core.itemPosition;

        if (!location) return;
        if (bot.options.mode === "creayun") {
          return;
        } else if (bot.options.mode === "savageFriends") {
          bot.core.commandBlock(command, location, 2, 0b101);

          bot.core.incrementCurrentBlock();
        } else {
          if (bot.core.usePlacedCommandBlock) {
            bot.core.commandBlock(command, itemPosition, 1, 5);
            bot.core.incrementCurrentBlock();
          } else {
            bot.core.commandBlock(command, location, 1, 5);
            bot.core.incrementCurrentBlock();
          }
        }
      },

      async runTracked(command, source) {
        const location = bot.core.currentBlock();

        const transactionId = Math.floor(Math.random() * 10000);

        bot.core.commandBlock(command, location, 1, 0b101);

        bot.core.incrementCurrentBlock();

        await sleep(100);

        bot._client.write("query_block_nbt", { location, transactionId });

        bot.on("packet.nbt_query_response", async (data) => {
          try {
            if (data.transactionId == transactionId) {
              if (data?.nbt?.value?.LastOutput) {
                const output = convertNbtComponentToJson(
                  null,
                  data.nbt.value.LastOutput.value.extra,
                );
                source.sendFeedback(output);
              }
            }
          } catch (e) {
            console.log(e.stack);
          }
        });
      },
    };

    let timer;
    bot.on("packet.block_change", (data) => {
      try {
        if (
          data?.location?.x === bot?.core?.position?.x &&
          data?.location?.y === bot?.core?.position?.y &&
          data?.location?.z === bot?.core?.position?.z &&
          data.type === 0
        ) {
          bot.core.move(bot.position);
        }
      } catch (e) {
        bot.console.warn(e.toString());
      }
    });

    bot.on("move", async () => {
      try {
        if (bot.options.mode === "savageFriends") {
          await sleep(2000);
          bot.chat.command("/removenear commandblock");
          await sleep(2000);
          bot.core.move(bot.position);
        } else {
          bot.core.move(bot.position);
        }
      } catch (e) {
        bot.console.warn(e.toString());
      }
    });

    bot.on("packet.login", () => {
      timer = setInterval(() => {
        bot.core.move(bot.position);
      }, config.core.refillInterval);
    });

    bot.on("end", () => {
      if (timer) clearInterval(timer);
    });
  }
}
module.exports = commandCore;
