const fs = require("fs");
const path = require("path");
async function loadPlugins(bot, options, config, discordClient) {
  bot.plugins = [];
  for (const filename of fs.readdirSync(path.join(__dirname, "../", "plugins"))) {
    try {
      if (filename.endsWith(".js")) {
        const plugin = require(path.join(__dirname, "../plugins", filename));
        bot.plugins.push(module);
        new plugin({ bot, options, config, discordClient });
      }
    } catch (error) {
      console.error(`Failed to load plugin ${filename} due to error`);
      console.error(`\x1b[31m${error.stack}\x1b[0m`);
    }
  }
}
module.exports = loadPlugins;
