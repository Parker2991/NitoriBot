const fs = require("fs");
const path = require("path");
async function loadModules (bot, config) {
  for (const filename of fs.readdirSync(path.join(__dirname, '../', 'modules'))) {
    try {
      if (filename.endsWith(".js")) {
        const module = require(path.join(__dirname, '../modules', filename));
        if (module.data.enabled === false) return;
        module.inject({ bot, config });
        bot.emit('modules', module.data);
      }
    } catch (error) {
      console.error(`Failed to load module ${filename} due to error`);
      console.error(`\x1b[31m${error.stack}\x1b[0m`);
    }
  }
}
module.exports = loadModules;
