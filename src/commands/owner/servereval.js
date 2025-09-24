const CommandError = require("../../command_util/command_error");
const { stylize } = require("../../util/stylizeEval");
const util = require("util");
const CommandContext = require("../../command_util/command_context");
const CommandTrustLevel = require("../../command_util/command_trust_level");
const trustLevel = new CommandTrustLevel();

class servereval extends CommandContext {
  constructor() {
    super("servereval", ["se"], "run code unisolated", 4, ["<code>"], true);
  }
  execute(context) {
    const bot = context.bot;
    const source = context.source;
    const config = context.config;
    const discordClient = context.discordClient;
    const args = context.arguments;
    const script = args.slice(1).join(" ");
    const { MessageBuilder } = require("prismarine-chat")(bot.options.version);
    try {
      if (source.sources.console) {
        bot.console.log(
          bot
            .getMessageAsPrismarine({
              text: util.inspect(eval(args.join(" ")), { stylize }),
            })
            ?.toAnsi(),
        );
      } else if (bot.options.mode === "savageFriends") {
        bot.core.run(
          `minecraft:tellraw @a ${JSON.stringify(util.inspect(eval(script), { stylize }).substring(0, 32700))}`,
        );
      } else {
        bot.tellraw(`@a[name="${source.player.profile.name}"]`, {
          text: `${util.inspect(eval(script), { stylize }).substring(0, 32700)}`,
        });
      }
    } catch (e) {
      throw new CommandError(e.toString());
    }
  }
}

module.exports = servereval
