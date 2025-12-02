class chat {
  constructor (context) {
    const bot = context.bot;
    const config = context.config;

    bot.on('packet.player_chat', (data: any) => {
      
    })

    bot.on('packet.system_chat', (data: any) => {

    });

    bot.on('packet.system_chat', (data: any) => {

    })
  }
}

export = chat