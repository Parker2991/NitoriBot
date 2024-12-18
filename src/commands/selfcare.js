module.exports = {
  data: {
    name: "selfcare",
    enabled: true,
    aliases: [
      "sc",
      "selfcaretoggle",
      "sctoggles"
    ],
    description: "toggle the selfcare",
    usages: [
      "vanish enable/disable",
      "mute enable/disable",
      "cspy enable/disable",
      "gamemode enable/disable",
      "op enable/disable",
      "icu enable/disable",
      "god enable/disable",
      "nick enable/disable",
      "username enable/disable",
      "prefix enable/disable"
    ]
  },
  execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    const config = context.config;
/*
  vanish: true
  mute: true
  cspy: true
  gamemode: true
  op: true
  icu: true
  god: true
  nick: true
  username: true
  prefix: true
*/
    if (!args && !args[0] && !args[1]) return;

    switch (args[0]?.toLowerCase()) {
      case "vanish":
        switch (args[1]) {
          case "enable":
            config.selfcare.vanish = true;
            bot.console.command('enabling vanish selfcare');
          break;
          case "disable":
            config.selfcare.vanish = false;
            bot.console.command('disabling vanish selfcare');
          break;
          default:
            bot.console.command(`Vanish selfcare is currently ${config.selfcare.vanish}`);
        }
      break;
      case "prefix":
        switch (args[1]) {
          case "enable":
            config.selfcare.prefix = true;
            bot.console.command('enabling prefix selfcare');
          break;
          case "disable":
            config.selfcare.prefix = false;
            bot.console.command('disabling prefix selfcare');
          break;
          default:
            bot.console.command(`Prefix selfcare is currently ${config.selfcare.prefix}`);
        }
      break;
      case "username":
        switch (args[1]) {
          case "enable":
            config.selfcare.username = true;
            bot.console.command('enabling username selfcare');
          break;
          case "disable":
            config.selfcare.username = false;
            bot.console.command('disabling username selfcare');
          break;
          default:
            bot.console.command(`Username selfcare is currently ${config.selfcare.username}`);
        }
      break;
      case "nick":
        switch (args[1]) {
          case "enable":
            config.selfcare.nick = true;
            bot.console.command('enabling nick selfcare');
          break;
          case "disable":
            config.selfcare.nick = false;
            bot.console.command('disabling nick selfcare');
          break;
          default:
            bot.console.command(`Nick selfcare is currently ${config.selfcare.nick}`);
        }
      break;
      case "mute":
        switch (args[1]) {
          case "enable":
            config.selfcare.mute = true;
            bot.console.command('enabling mute selfcare');
          break;
          case "disable":
            config.selfcare.mute = false;
            bot.console.command('disabling mute selfcare');
          break;
          default:
            bot.console.command(`Mute selfcare is currently ${config.selfcare.mute}`);
        }
      break;
      case "cspy":
        switch (args[1]) {
          case "enable":
            config.selfcare.cspy = true;
            bot.console.command('enabling cspy selfcare');
          break;
          case "disable":
            config.selfcare.cspy = false;
            bot.console.command('disabling cspy selfcare');
          break;
          default:
            bot.console.command(`Cspy selfcare is currently ${config.selfcare.cspy}`);
        }
      break;
      case "god":
        switch (args[1]) {
          case "enable":
            config.selfcare.god = true;
            bot.console.command('enabling god selfcare');
          break;
          case "disable":
            config.selfcare.god = false;
            bot.console.command('disabling god selfcare');
          break;
          default:
            bot.console.command(`God selfcare is currently ${config.selfcare.god}`);
        }
      break;
      case "icu":
        switch (args[1]) {
          case "enable":
            config.selfcare.icu = true;
            bot.console.command('enabling icu selfcare');
          break;
          case "disable":
            config.selfcare.icu = false;
            bot.console.command('disabling icu selfcare');
          break;
          default:
            bot.console.command(`Icu selfcare is currently ${config.selfcare.icu}`);
        }
      break;
      case "gamemode":
        switch (args[1]) {
          case "enable":
            config.selfcare.gamemode = true;
            bot.console.command('enabling gamemode selfcare');
          break;
          case "disable":
            config.selfcare.gamemode = false;
            bot.console.command('disabling gamemode selfcare');
          break;
          default:
            bot.console.command(`Gamemode selfcare is currently ${config.selfcare.gamemode}`);
        }
      break;
      case "op":
        switch (args[1]) {
          case "enable":
            config.selfcare.op = true;
            bot.console.command('enabling op selfcare');
          break;
          case "disable":
            config.selfcare.op = false;
            bot.console.command('disabling op selfcare');
          break;
          default:
            bot.console.command(`Op selfcare is currently ${config.selfcare.vanish}`);
        }
      break;
      default:
        bot.console.command('invalid argument');
    }
  }
}
