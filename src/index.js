const createBot = require("./bot.js");
const readline = require("readline");
const fs = require("fs");
const path = require("path");
const loadModules = require("./util/loadModules");
const loadPlugins = require("./util/loadPlugins");
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
const CommandSource = require("./command_util/command_source");

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

  bots.push(bot);
  bot.bots = bots;
  loadModules(bot, options, config, discordClient);
  bot.console.readlineInterface(rl);
}
loadPlugins(bot, options, config, discordClient);
discordClient.on("messageCreate", (message) => {
  try {
    if (message.author.id === bot.discord.client.user.id) return;

    const ChatMessage = require("prismarine-chat")("1.20.2");
    bot.getMessageAsPrismarine = (message) => {
      return new ChatMessage(message);
    };
    config.prefixes.map((prefix) => {
      if (message.content.startsWith(prefix)) {
        const source = new CommandSource(
          {
            profile: {
              name: `${message?.member.nickname || message?.author.displayName}`,
            },
          },
          {
            discord: true,
            console: false,
          },
          false,
          message,
        );

        bot.commandManager.executeString(
          source,
          message.content.substring(prefix.length),
        );
        return;
      }
    });
  } catch (e) {
    console.log(e.stack);
  }
});
