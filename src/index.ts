import { Bot } from "./Bot";
const irc = require('irc-framework');
const config = require('../config.js');

const ircClient = new irc.Client();

ircClient.connect(config.irc.client);

let bots = [];

for (const options of config.bots) {
  let bot = Bot.Bot(options, config, ircClient)
}
