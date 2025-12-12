import fs from "fs";
import path from "path"
import { createBot } from "./bot"
import { parseToml } from './util/parseToml';
import loadModules from './util/loadModules';

const config = parseToml('./config.toml');
const bots = []
console.log(config)
for (const options of config.bots) {
  const bot = new createBot(options);
  loadModules(bot, config)
  bots.push(bot);
  bot.bots = bots;
}