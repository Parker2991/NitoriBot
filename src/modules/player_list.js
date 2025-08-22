const convertNbtComponentToJson = require("../util/nbt_parser");

class player_list {
  constructor(context) {
    const bot = context.bot;
    const config = context.config;
    const options = context.options;
    bot.players = [];

    bot.on("packet.player_info", async (packet) => {
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

    bot.on("packet.player_remove", async ({ players }) => {
      // players has uuids of the players
      let player_completion = (
        await bot.tab_complete("scoreboard players add ")
      ).filter((_) => _.tooltip == undefined); // exclude @a, @r, @s, @e, @p -aaa
      bot.players.forEach(async (player) => {
        if (!players.includes(player.uuid)) return;

        const a = player_completion.filter(
          (_) => _.match == player.profile.name,
        );
        if (a.length >= 1) {
          player.vanished = true;
          
        } else {
          bot.players = bot.players.filter((_) => _.uuid != player.uuid);
          bot.emit("player_left", player);
        } if (player.vanished === true) bot.emit("player_left", players)
      });
    });

    function addPlayer(entry) {
      bot.players = bot.players.filter((_entry) => _entry.uuid !== entry.uuid);
      bot.players.push({
        uuid: entry.uuid,
        mcUUID: "",
        profile: {
          name: entry.player.name,
          properties: entry.player.properties,
        },
        chatSession: undefined,
        gamemode: undefined,
        listed: undefined,
        latency: undefined,
        displayName: undefined,
        vanished: false,
      });
      bot.emit('player_join', entry)
    }

    function initializeChat(entry) {}

    function updateGamemode(entry) {
      const target = bot.players.find((_entry) => _entry.uuid === entry.uuid);
      if (!target) return;

      target.gamemode = entry.gamemode;
    }

    function updateListed(entry) {
      const target = bot.players.find((_entry) => _entry.uuid === entry.uuid);
      if (!target) return;
      target.listed = entry.listed;
    }

    function updateLatency(entry) {
      const target = bot.players.find((_entry) => _entry.uuid === entry.uuid);
      if (!target) return;

      target.latency = entry.latency;
    }

    function updateDisplayName(entry) {
      const target = bot.players.find((_entry) => _entry.uuid === entry.uuid);
      if (!target) return;

      try {
        target.displayName = JSON.parse(entry.displayName);
      } catch {
        // do nothing
      }
    }

    bot.on("end", () => (bot.players = []));
  }
}
module.exports = player_list;
