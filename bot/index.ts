import fs from "fs";;
import path from "path";
import readline from 'readline'
import { createBot } from "./bot";
import { parseToml } from './util/parseToml';
import loadModules from './util/loadModules';
import version from './data/version.json'
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
})

let debugEnabled = false;

if (process.argv[2] === "--debug"?.toLowerCase()) {
  debugEnabled = true
}

if (debugEnabled) console.log(`Starting FNFBoyfriendBot ${version.codename} ${version.version} Build: ${version.build}`)
else console.log('Starting FNFBoyfriendBot....');

const config = parseToml('./config.toml');
const bots = [];

for (const options of config.bots) {
  const bot = new createBot(options);
  bot.debugEnabled = debugEnabled;
  bots.push(bot);
  bot.bots = bots;
  loadModules(bot, config, rl);
  bot?.console?.interface(rl);
}