import { Bot } from "./Bot";

const config = require('../config.js');

let bots = [];

for (const options of config.bots) {
  //let bot = new Bot.Bot(options, config)//Bot.Bot().Bot//(options, config);
  let bot = Bot.Bot(options, config)
  //bot.bots.push(bot);
  //console.log(bot)
}