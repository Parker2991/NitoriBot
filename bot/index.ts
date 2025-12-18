import fs from "fs";
import path from "path"
import readline from 'readline';
import { createBot } from "./bot";
import { parseToml } from './util/parseToml';
import version from './resources/version.json';
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
  let bot = new createBot(options, config);
  bots.push(bot);
  bot.bots = bots;
  bot.console.interface(rl)
}