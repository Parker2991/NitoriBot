const CommandContext = require("../../command_util/command_context");
const CommandTrustLevel = require("../../command_util/command_trust_level");
const CommandError = require("../../command_util/command_error");
const trustLevel = new CommandTrustLevel();

class auth extends CommandContext {
  constructor() {
    super(
      "auth",
      ["authentication"],
      "auth through the bot so you dont need to use a hash everytime",
      trustLevel.trusted,
      [""],
      false,
    );
  }
  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const source = context.source;
    const config = context.config;
    const uuid = source.player.uuid;
    let component = []

    if (bot.auth.list.find((e) => e?.player === uuid))
      throw new CommandError(`You are already authed!`);

    if (source.player.hash === "trusted") {
      source.player.authed = true;
      bot.auth.add({
        player: uuid,
        trustLevel: "trusted",
      });

      component.push({
        text: "Authed as Trusted",
        color: config.colors.help.trusted
      })
    } else if (source.player.hash === "admin") {
      source.player.authed = true;
      bot.auth.add({
        player: uuid,
        trustLevel: "admin",
      });

      component.push({
        text: "Authed as Admin",
        color: config.colors.help.admin
      })
    } else if (source.player.hash === "owner") {
      source.player.authed = true;
      bot.auth.add({
        player: source.player.uuid,
        trustLevel: "owner",
      });
      component.push({
        text: "Authed as Owner",
        color: config.colors.help.owner
      })
    }

    source.sendFeedback(component)
  }
}

module.exports = auth;
