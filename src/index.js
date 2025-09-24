const createBot = require("./bot.js");
const readline = require("readline");
const fs = require("fs");
const path = require("path");
const loadModules = require("./util/loadModules");
const { Client, GatewayIntentBits, Partials } = require("discord.js");
const fixansi = require("./util/ansi");
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
const CommandSource = require("./commandUtil/CommandSource");

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
const discord = [
  require('./discord/directMessages.js'),
]

for (const file of discord) new file({ bot, config, discordClient })

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

        source.sendFeedback = input => {
          message?.reply(`\`\`\`ansi\n${fixansi(bot.getMessageAsPrismarine(input)?.toAnsi()?.replaceAll('`', '`\u200b').substring(0, 1780))}\`\`\``);
        }
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
