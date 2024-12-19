const fs = require('fs');
const { load } = require('js-yaml');
const path = require('path');
const { WebhookClient } = require('discord.js');


if (!fs.existsSync(path.join(__dirname, "../config.yml"))) {
  console.warn("Config not found creating config from the default config");
  fs.copyFileSync(
    path.join(__dirname, "./data/default_config.yml"),
    path.join(__dirname, "../config.yml")
  )
}

if (!fs.existsSync(path.join(__dirname, "./modules/custom_chat.js"))) {
  console.warn("custom chat format not found recreating from default custom chat format");
  fs.copyFileSync(
    path.join(__dirname, "./data/default_custom_chat.js"),
    path.join(__dirname, "./modules/custom_chat.js")
  )
}

if (!fs.existsSync(path.join(__dirname, "./modules/exploits.js"))) {
  console.warn("exploits module not found recreating from default exploits");
  fs.copyFileSync(
    path.join(__dirname, "./data/default_exploits.js"),
    path.join(__dirname, "./modules/exploits.js")
  )
}

try {
  config = load(fs.readFileSync(path.join(__dirname, '../', 'config.yml')));
} catch (e) {
  console.error("Failed to load the config look below for the error stack");
  console.error(e.stack);
}

const webhook = new WebhookClient({ id: config.bots.testbot.id, token: config.bots.testbot.token });
webhook.send({ content: "amogus" });

let bot = require('./bot.js')({ config });

require('./util/loadModules')(bot, config, webhook);
