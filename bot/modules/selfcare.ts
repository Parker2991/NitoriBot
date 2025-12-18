export class selfcare {
  constructor (context: any) {
    const bot = context.bot;
    const config = context.config;
    const options = bot.options;

    bot.selfcare = {
      permission: 2,
      gamemode: 0,
    };

    const entity = [
      require('../selfcare/entity/permission'),
      require('../selfcare/entity/gamemode')
    ];

    for (const file of entity) file.default(bot, config);

    let timer: any;

    bot.on('packet.login', (packet: any) => {
      timer = setInterval(() => {
        if (bot.selfcare.permission < 2) bot.chat.command("minecraft:op @s[type=player]");
        else if (bot.selfcare.gamemode !== 1) bot._client.write("change_gamemode", { mode: 1 })
      }, options.scInterval)
    });

    bot.on('end', () => {
      if (timer) clearInterval(timer);
    })
  }
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        