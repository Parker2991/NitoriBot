const createBot = require("./bot.js");
const readline = require("readline");
const fs = require("fs");
const path = require("path");
const loadModules = require("./util/loadModules");
const { Client, GatewayIntentBits, Partials } = require("discord.js");
const { MessageContent, GuildMessages, Guilds, DirectMessages } =
  GatewayIntentBits;

const discordClient = new Client({
  intents: [
    Guilds,
    GuildMessages,
    MessageContent,
    GatewayIntentBits.DirectMessages,
  ],
  partials: [Partials.Channel],
});

console.log("Starting FNFBoyfriendBot");

if (!fs.existsSync(path.join(__dirname, "../config.js"))) {
  console.warn("Config not found creating config from the default config");
  fs.copyFileSync(
    path.join(__dirname, "./data/default_config.js"),
    path.join(__dirname, "../config.js"),
  );
}

const config = require("../config.js");

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
});

if (config.discord.enabled) discordClient.login(config.discord.token);

const bots = [];
let options;
let bot;

for (const botOptions of config.bots) {
  options = botOptions;
  bot = new createBot(options, config);
  bot.translations = require('./data/translations.json')
  bots.push(bot);
  bot.bots = bots;
  loadModules(bot, options, config, discordClient);
  bot.console.readlineInterface(rl);
}