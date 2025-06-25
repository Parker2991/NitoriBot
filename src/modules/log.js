module.exports = {
  data: {
    descripton: "logs chat to console",
    enabled: true,
    name: "log",
    type: "console"
  },
  inject (context) {
    const bot = context.bot;
    bot.on('playerChat', (message) => {
      bot.console.log(message);
      //console.log(message);
    });

    bot.on('profilelessChat', (message) => {
      bot.console.log(message);
//      console.log(message);
    });

    bot.on('systemChat', (message) => {
      bot.console.log(message);
//      console.log(JSON.stringify(message));
    });

    bot.on('actionBar', (message) => {
    //  console.log(message);
    });

/*    bot.on('titleBar', (message) => {
      console.log(message);
    });*/
  }
}

