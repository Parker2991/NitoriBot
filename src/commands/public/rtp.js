const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");
const between = require('../../util/between');

class rtp extends CommandContext {
  constructor() {
    super(
      "rtp",
      [
        "randomtp",
        "tpr",
        "tprandom"
      ],
      "teleport to a random place. This was ported from FNFBoyfriendBot v4.x",
      trustLevel.public,
      [""],
      true
    );
  }

  execute(context) {
    const bot = context.bot;
    const source = context.source;
    const config = context.config;
    const translations = bot.translations;
    const x = between(-1_000_000, 1_000_000);
    const y = 100;
    const z = between(-1_000_000, 1_000_000);
    let component = [];

    bot.core.run(`essentials:tp ${source.player.uuid} ${x} ${y} ${z}`)

    component.push({
      translate: "fnfboyfriendbot.command.rtp",
      fallback: translations["fnfboyfriendbot.command.rtp"],
      color: config.colors.commands.primary,
      with: [
        { text: `${source.player.profile.name}`, color: config.colors.commands.secondary },
        { text: `${x}`, color: config.colors.integer },
        { text: `${y}`, color: config.colors.integer },
        { text: `${z}`, color: config.colors.integer }
      ]
    })

    bot.tellraw('@a', component)
  }
}

module.exports = rtp