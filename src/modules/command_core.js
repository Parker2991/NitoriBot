const nbt = require("prismarine-nbt");
const sleep = require("../util/sleep");
const convertNbtComponentToJson = require("../util/nbt_parser");

class CommandCoreModule {
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

      refill() {
        const pos = bot.core.position;
        const { start, end } = bot.core.area;
        const itemPosition = bot.core.itemPosition;

        if (!pos || !itemPosition) return;

        if (bot.options.mode === "savageFriends") {
          command = `minecraft:fill ${pos.x + start.x} ${pos.y + start.y} ${pos.z + start.z} ${pos.x + end.x} ${pos.y + end.y} ${pos.z + end.z} command_block`;
        } else {
          command = `minecraft:fill ${pos.x + start.x} ${pos.y + start.y} ${pos.z + start.z} ${pos.x + end.x} ${pos.y + end.y} ${pos.z + end.z} command_block{CustomName:'${JSON.stringify(config.core.name)}'}`;
        }

        if (config.core.itemRefill && bot.options.mode !== "savageFriends") {
          bot._client.write("set_creative_slot", {
            slot: 36,
            item: Item.toNotch(
              new Item(
                bot.registry.itemsByName.repeating_command_block.id,
                1,
                0,
              ),
            ),
            /*          item: {
            present: true,
//            itemId: 492,
            itemId: Item.toNotch(new Item(bot.registry.itemsByName.repeating_command_block.id, 1, 0)),
            itemCount: 1,
          },*/
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
            bot._client.write("update_command_block", {
              location: itemPosition,
              command,
              flags: 5,
              mode: 1,
            });
          }
        } else {
          bot.chat.command(`${command}`);
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

        bot.core.refill();
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

      run(command) {
        const location = bot.core.currentBlock();
        const itemPosition = bot.core.itemPosition;

        if (!location) return;
        if (bot.options.mode === "creayun") {
          return;
        } else if (bot.options.mode === "savageFriends") {
          bot._client.write("update_command_block", {
            command: command.substring(0, 32767),
            location: location,
            mode: 2,
            flags: 0b101,
          });

          bot.core.incrementCurrentBlock();
        } else {
          if (bot.core.usePlacedCommandBlock) {
            bot._client.write("update_command_block", {
              command: command.substring(0, 32767),
              location: itemPosition,
              mode: 1,
              flags: 5,
            });

            bot.core.incrementCurrentBlock();
          } else {
            bot._client.write("update_command_block", {
              command: command.substring(0, 32767),
              location,
              mode: 1,
              flags: 5,
            });

            bot.core.incrementCurrentBlock();
          }
        }
      },

      async runTracked(command, selector) {
        const location = bot.core.currentBlock();
        let transactionId = Math.floor(Math.random() * 10000);

        bot._client.write("update_command_block", {
          command: command.substring(0, 32767),
          location,
          flags: 0b101,
          mode: 1,
        });

        bot.core.incrementCurrentBlock();

        await sleep(100);

        bot._client.write("query_block_nbt", { location, transactionId });

        bot.on("packet.nbt_query_response", (data) => {
          try {
            if (data.transactionId == transactionId) {
              if (data?.nbt?.value?.LastOutput) {
                bot.tellraw(
                  "@a",
                  JSON.parse(data.nbt.value.LastOutput.value).extra,
                );
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

    bot.on("packet.multi_block_change", (data) => {
      try {
        if (bot.options.mode !== "kaboom") return;

        let broken = true;

        if (
          data.chunkCoordinates.x === Math.floor(bot.core.position.x / 16) &&
          data.chunkCoordinates.y === Math.floor(bot.core.position.y / 16) &&
          data.chunkCoordinates.z === Math.floor(bot.core.position.z / 16)
        ) {
          for (const state of data.records) {
            if (state >= 7906 && state >= 7917) broken = false;
            if (state >= 12527 && state >= 12538) broken = false;
            if (state >= 12515 && state >= 12526) broken = false;
            else broken = true;
          }

          if (broken === true) bot.core.move(bot.position);
        }
      } catch (e) {
        console.log(e.stack);
      }
    });
  }
}
module.exports = CommandCoreModule;
