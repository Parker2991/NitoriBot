import mc from 'minecraft-protocol';

export class reconnect {
  constructor (context: any) {
    const bot = context.bot;
    const config = context.config;
    const options = bot.options;
    bot.reconnectDelay = options.reconnectDelay;
    let client;

    bot.on('end', () => {
      setTimeout(() => {
        client = options.client ?? mc.createClient(options);
        bot._client = client;
        bot.emit("init_client", bot._client);
      }, bot.reconnectDelay);
    })
  }
}

/*
    bot.on("end", () => {
      setTimeout(() => {
        if (options.usernameGen) {
          client =
            options.client ??
            mc.createClient(options, (options.username = usernameGen()));
        } else {
          client = options.client ?? mc.createClient(options);
        }
        bot._client = client;
        bot.emit("init_client", bot._client);
      }, bot.reconnectDelay);
    });
  }
}
module.exports = reconnect;

*/