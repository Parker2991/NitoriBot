const fs = require('fs');
const { load } = require('js-yaml');
const path = require('path');

if (!fs.existsSync(path.join(__dirname, "../config.yml"))) {
  console.warn("Config not found creating config from the default config");
  fs.copyFileSync(
    path.join(__dirname, "./data/default_config.yml"),
    path.join(__dirname, "../config.yml")
  )
}

try {
  config = load(fs.readFileSync(path.join(__dirname, '../', 'config.yml')));
} catch (e) {
  console.error("Failed to load the config look below for the error stack");
  console.error(e.stack);
}

let bot = require('./bot.js')({ config });

require('./util/loadModules')(bot, config);
