import fs from "fs";
import path from "path"
import readline from 'readline';
import { Bot } from "./Bot";
import { ParseToml } from './util/ParseToml';
import version from './resources/Version.json';
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

const config = ParseToml('./config.toml');
const bots = [];

for (const options of config.bots) {
  let bot = new Bot(options, config, rl);
  bots.push(bot);
  bot.bots = bots;
//  bot?.console?.interface(rl)
}
