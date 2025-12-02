import { parse } from 'yaml';
import fs from "fs";
import path from "path"
import createBot from "./bot"
import loadModules from './util/loadModules';

const file = fs.readFileSync(path.join(__dirname, '../config.yml'), "utf8");
const config = parse(file)
const bots = []
for (const options of config.bots) {
  const bot = new createBot(options)

  //const loadModules = lm
  //console.log(loadModules
  bots.push(bot);

  bot.bots = bots;

  new loadModules(bot, config)

/*  bot.on('packet.player_chat', (packet: any) => {
    console.log(packet)
  })*/
}