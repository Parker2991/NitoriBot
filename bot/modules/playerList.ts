const convertNbtComponentToJson = require("../util/convertNbtComponentToJson");
const entityUUID = require('../util/entityUUID');

// taken from FNFBoyfriendBot v8.0.0
console.log(entityUUID)
export default class playerList {
  constructor(context: any) {
    const bot = context.bot;
    bot.players = [];

    bot.on("packet.player_info", (packet: any) => {
      const actions = [];
      if (packet.action._value & 0b000001) actions.push(addPlayer);
      if (packet.action._value & 0b000010) actions.push(initializeChat);
      if (packet.action._value & 0b000100) actions.push(updateGamemode);
      if (packet.action._value & 0b001000) actions.push(updateListed);
      if (packet.action._value & 0b010000) actions.push(updateLatency);
      if (packet.action._value & 0b100000) actions.push(updateDisplayName);
      for (const entry of packet.data) {
        for (const action of actions) {
          action(entry);
        }
      }
    });

    bot.on("packet.player_remove", async ({ players }: { players: any    }) => {
      // players has uuids of the players
      let player_completion = (
        await bot.tab_complete("scoreboard players add ")
      ).filter((_: any) => _.tooltip == undefined); // exclude @a, @r, @s, @e, @p -aaa
      bot.players.forEach(async (player: any) => {
        if (!players.includes(player.uuid)) return;

        const a = player_completion.filter(
          (_: any) => _.match == player.profile.name,
        );
        if (a.length >= 1) {
          player.vanished = true;
        } else {
          bot.players = bot.players.filter((_: any) => _.uuid != player.uuid);
        }
      });
    });

    function addPlayer(entry: any) {
      bot.players = bot.players.filter((_entry: any) => _entry.uuid !== entry.uuid);
      bot.players.push({
        uuid: entry.uuid,
        entityUuid: entityUUID(entry.uuid),
        profile: {
          name: entry.player.name,
          properties: entry.player.properties,
        },
        chatSession: undefined,
        gamemode: undefined,
        listed: undefined,
        latency: undefined,
        displayName: undefined,
        prefix: undefined,
        vanished: false,
      });

      //bot.emit('player_info', entry)
    }

    function initializeChat(entry: any) {}

    function updateGamemode(entry: any) {
      const target = bot.players.find((_entry: any) => _entry.uuid === entry.uuid);
      if (!target) return;

      target.gamemode = entry.gamemode;
    }

    function updateListed(entry: any) {
      const target = bot.players.find((_entry: any) => _entry.uuid === entry.uuid);
      if (!target) return;
      target.listed = entry.listed;
    }

    function updateLatency(entry: any) {
      const target = bot.players.find((_entry: any) => _entry.uuid === entry.uuid);
      if (!target) return;

      target.latency = entry.latency;
    }

    function updateDisplayName(entry: any) {
      const target = bot.players.find((_entry: any) => _entry.uuid === entry.uuid);
      if (!target) return;

      try {
        target.displayName = convertNbtComponentToJson(null, entry.displayName);
      //  if (bot.options.mode === "kaboom") target.prefix = convertNbtComponentToJson(null, entry.displayName).extra[0]
      } catch {
        // do nothing
      }
    }

    bot.on("end", () => (bot.players = []));
  }
}