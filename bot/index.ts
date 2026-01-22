import { Bot } from "./Bot";

const config = require('../config.js');

let bots = [];

for (const options of config.bots) {
  let bot = new Bot(options, config);
  bot.bots.push(bot);
}