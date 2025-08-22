const os = require("os");
const CommandError = require("../../command_util/command_error");
const fs = require("fs");
const botInfo = require("../../data/info.json");
const fixansi = require("../../util/ansi.js");
const { execSync } = require("child_process");
const CommandContext = require("../../command_util/command_context");
const CommandTrustLevel = require("../../command_util/command_trust_level");
const trustLevel = new CommandTrustLevel();

function format(seconds) {
  function pad(s) {
    return (s < 10 ? "0" : "") + s;
  }
  var hours = Math.floor(seconds / (60 * 60));
  var minutes = Math.floor((seconds % (60 * 60)) / 60);
  var seconds = Math.floor(seconds % 60);
  return (
    pad(`${hours} Hours`) +
    " " +
    pad(`${minutes} Minutes`) +
    " " +
    pad(`${seconds} Seconds`)
  );
}

class info extends CommandContext {
  constructor() {
    super(
      "info",
      ["information"],
      "check the bots info",
      trustLevel.public,
      [
        "about",
        "config <client, discord, options, all>",
        "contributors/credits",
        "discord",
        "loaded",
        "usages <bot, server, all>",
        "uptimes/uptime <bot, server, all>",
        "server",
        "version/ver",
      ],
      false,
    );
  }

  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const config = context.config;
    const discordClient = context.discordClient;
    const source = context.source;
    let component = [];

    switch (args[0]?.toLowerCase()) {
      case "about":
        component.push({
          text: `FNFBoyfriendBot is a kaboom bot created by Parker2991\nThe source code and changelog can be found here ${botInfo.buildstring.url}`,
          color: `${config.colors.commands.primary}`,
          translate: "",
          hoverEvent: {
            action: "show_text",
            value: [
              {
                text: "click here to view bots source code",
                color: `${config.colors.commands.primary}`,
              },
            ],
          },
          clickEvent: {
            action: "open_url",
            value: `${botInfo.buildstring.url}`,
          },
        });
        break;
      case "config":
        if (bot.options.isKaboom) {
          mode = "Kaboom";
        }
        if (bot.options.useChat && bot.options.isKaboom) {
          mode = "Kaboom/Coreless";
        }
        if (bot.options.isSavage) {
          mode = "Savage";
        }
        if (bot.options.isCreayun) {
          mode = "Creayun";
        }
        switch (args.slice(1).join(" ")?.toLowerCase()) {
          case "client":
            component.push({
              translate: "%s: %s:%s\n%s: %s\n%s: %s\n%s: %s",
              color: config.colors.commands.tertiary,
              with: [
                { text: "Server", color: config.colors.commands.primary },
                {
                  text: `${bot.options.host}`,
                  color: config.colors.commands.secondary,
                },
                { text: `${bot.options.port}`, color: config.colors.integer },
                { text: "Server Name", color: config.colors.commands.primary },
                {
                  text: `${bot.options.serverName}`,
                  color: config.colors.commands.secondary,
                },
                {
                  text: "Minecraft Username",
                  color: config.colors.commands.primary,
                },
                {
                  text: `${bot.options.username}`,
                  color: config.colors.commands.secondary,
                },
                { text: "Version", color: config.colors.commands.primary },
                {
                  text: `${bot.options.version}`,
                  color: config.colors.integer,
                },
              ],
            });
            break;
          case "discord":
            if (!config.discord.enabled || discordClient.user === null) {
              throw new CommandError(
                "Token is incorrect or discord isnt enabled!",
              );
            } else {
              component.push({
                translate: "%s: %s\n%s: %s",
                color: config.colors.commands.tertiary,
                with: [
                  {
                    text: "Discord Username",
                    color: config.colors.commands.primary,
                  },
                  {
                    text: `${discordClient.user.tag}`,
                    color: config.colors.commands.secondary,
                  },
                  {
                    text: "Discord Channel",
                    color: config.colors.commands.primary,
                  },
                  {
                    text: `${bot.discord?.channel?.name}`,
                    color: config.colors.commands.secondary,
                  },
                ],
              });
            }
            break;
          case "options":
            component.push({
              translate: "%s: %s\n%s: %s\n%s: %s",
              color: config.colors.commands.tertiary,
              with: [
                { text: "Server Count", color: config.colors.commands.primary },
                { text: `${bot.bots.length}`, color: config.colors.integer },
                { text: "Prefixes", color: config.colors.commands.primary },
                {
                  text: `${config.prefixes.map((e) => e + " ").join(" ")}`,
                  color: config.colors.commands.secondary,
                },
                { text: "Mode", color: config.colors.commands.primary },
                {
                  text: `${bot.options.mode}`,
                  color: config.colors.commands.secondary,
                },
              ],
            });
            break;
          case "all":
            component.push({
              translate:
                "%s: %s:%s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
              color: config.colors.commands.tertiary,
              with: [
                { text: "Server", color: config.colors.commands.primary },
                {
                  text: `${bot.options.host}`,
                  color: config.colors.commands.secondary,
                },
                { text: `${bot.options.port}`, color: config.colors.integer },
                { text: "Server Name", color: config.colors.commands.primary },
                {
                  text: `${bot.options.serverName}`,
                  color: config.colors.commands.secondary,
                },
                {
                  text: "Minecraft Username",
                  color: config.colors.commands.primary,
                },
                {
                  text: `${bot.options.username}`,
                  color: config.colors.commands.secondary,
                },
                { text: "Version", color: config.colors.commands.primary },
                {
                  text: `${bot.options.version}`,
                  color: config.colors.commands.secondary,
                },
                {
                  text: "Discord Username",
                  color: config.colors.commands.primary,
                },
                {
                  text: `${discordClient.user?.tag}`,
                  color: config.colors.commands.secondary,
                },
                {
                  text: "Discord Channel",
                  color: config.colors.commands.primary,
                },
                {
                  text: `${bot.discord.channel?.name}`,
                  color: config.colors.commands.secondary,
                },
                { text: "Server Count", color: config.colors.commands.primary },
                { text: `${bot.bots.length}`, color: config.colors.integer },
                { text: "Prefixes", color: config.colors.commands.primary },
                {
                  text: `${config.prefixes.map((e) => e + " ").join(" ")}`,
                  color: config.colors.commands.secondary,
                },
                { text: "Mode", color: config.colors.commands.primary },
                {
                  text: `${bot.options.mode}`,
                  color: config.colors.commands.secondary,
                },
              ],
            });
            break;
          default:
            throw new CommandError({
              translate: "command.unknown.argument",
              color: "dark_red",
            });
        }
        break;
      case "contributors":
      case "credits":
        component.push({
          translate: "%s%s - %s\n%s:\n%s\n%s\n%s\n%s\n%s %s\n%s\n%s",
          color: config.colors.commands.tertiary,
          with: [
            { text: "Parker", color: "dark_red" },
            { text: "2991", color: "black" },
            { text: "Owner" },
            { text: "Contributors" },
            { text: "_ChipMC_", color: "dark_blue" },
            { text: "chayapak", color: "yellow" },
            { text: "_yfd", color: "light_purple" },
            { text: "aaa", color: "gold" },
            { text: "Morgan", color: "green" },
            { text: "Ankan", color: "dark_green" },
            { text: "TurtleKid", color: "green" },
            { text: "Ploat/ImGloriz", color: "#cd8ccb" },
          ],
        });
        break;
      case "discord":
        component.push({
          text: `the discord server invite is ${config.discord.invite}`,
          color: config.colors.commands.primary,
          translate: "",
          hoverEvent: {
            action: "show_text",
            value: [
              {
                text: "click here to join the discord server!",
                color: config.colors.commands.secondary,
              },
            ],
          },
          clickEvent: {
            action: "open_url",
            value: `${config.discord.invite}`,
          },
        });
        break;
      case "loaded":
        component.push({
          translate: "%s %s\n%s %s",
          color: config.colors.commands.tertiary,
          with: [
            {
              text: `${bot.commandManager.commandlist.length}`,
              color: config.colors.integer,
            },
            { text: "Commands", color: config.colors.commands.primary },
            { text: `${bot.modules.length}`, color: config.colors.integer },
            { text: "Modules", color: config.colors.commands.primary },
          ],
        });
        break;
      case "usages":
      case "usage":
        switch (args.slice(1).join(" ")?.toLowerCase()) {
          case "bot":
            component.push({
              translate: "%s: %s %s / %s %s",
              color: config.colors.commands.tertiary,
              with: [
                {
                  text: "Bot Memory",
                  color: config.colors.commands.primary,
                },
                {
                  text: `${Math.floor(process.memoryUsage().heapUsed / 1048576)}`,
                  color: config.colors.integer,
                },
                { text: "MiB", color: config.colors.commands.secondary },
                {
                  text: `${Math.floor(process.memoryUsage().heapTotal / 1048576)}`,
                  color: config.colors.integer,
                },
                { text: "MiB", color: config.colors.commands.secondary },
              ],
            });
            break;
          case "server":
            component.push({
              translate: "%s: %s %s / %s %s",
              color: config.colors.commands.tertiary,
              with: [
                {
                  text: "Server Memory",
                  color: config.colors.commands.primary,
                },
                {
                  text: `${Math.floor(os.totalmem() / 1048576) - Math.floor(os.freemem() / 1048576)}`,
                  color: config.colors.integer,
                },
                { text: "MB", color: config.colors.commands.secondary },
                {
                  text: `${Math.floor(os.totalmem() / 1048576)}`,
                  color: config.colors.integer,
                },
                { text: "MB", color: config.colors.commands.secondary },
              ],
            });
            break;
          case "all":
            component.push({
              translate: "%s: %s %s / %s %s\n%s: %s %s / %s %s",
              color: config.colors.commands.tertiary,
              with: [
                {
                  text: "Server Memory",
                  color: config.colors.commands.primary,
                },
                {
                  text: `${Math.floor(os.totalmem() / 1048576) - Math.floor(os.freemem() / 1048576)}`,
                  color: config.colors.integer,
                },
                { text: "MB", color: config.colors.commands.secondary },
                {
                  text: `${Math.floor(os.totalmem() / 1048576)}`,
                  color: config.colors.integer,
                },
                { text: "MB", color: config.colors.commands.secondary },
                {
                  text: "Bot Memory",
                  color: config.colors.commands.primary,
                },
                {
                  text: `${Math.floor(process.memoryUsage().heapUsed / 1048576)}`,
                  color: config.colors.integer,
                },
                { text: "MB", color: config.colors.commands.secondary },
                {
                  text: `${Math.floor(process.memoryUsage().heapTotal / 1048576)}`,
                  color: config.colors.integer,
                },
                { text: "MB", color: config.colors.commands.secondary },
              ],
            });
            break;
          default:
            throw new CommandError({
              translate: "command.unknown.argument",
              color: "dark_red",
            });
        }
        break;
      case "uptimes":
        switch (args.slice(1).join(" ")?.toLowerCase()) {
          case "bot":
            component.push({
              translate: "%s: %s",
              color: config.colors.commands.tertiary,
              with: [
                { text: "Bot Uptime", color: config.colors.commands.primary },
                {
                  text: `${format(process.uptime())}`,
                  color: config.colors.commands.secondary,
                },
              ],
            });
            break;
          case "server":
            component.push({
              translate: "%s: %s",
              color: config.colors.commands.tertiary,
              with: [
                {
                  text: "Server Uptime",
                  color: config.colors.commands.primary,
                },
                {
                  text: `${format(os.uptime())}`,
                  color: config.colors.commands.secondary,
                },
              ],
            });
            break;
          case "all":
            component.push({
              translate: "%s: %s\n%s: %s",
              color: config.colors.commands.tertiary,
              with: [
                { text: "Bot Uptime", color: config.colors.commands.primary },
                {
                  text: `${format(process.uptime())}`,
                  color: config.colors.commands.secondary,
                },
                {
                  text: "Server Uptime",
                  color: config.colors.commands.primary,
                },
                {
                  text: `${format(os.uptime())}`,
                  color: config.colors.commands.secondary,
                },
              ],
            });
            break;
            throw new CommandError({
              translate: "command.unknown.argument",
              color: "dark_red",
            });
        }
        break;
      case "server":
        component.push({
          translate:
            "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
          color: config.colors.commands.tertiary,
          with: [
            { text: "Hostname", color: config.colors.commands.primary },
            {
              text: `${os.hostname()}`,
              color: config.colors.commands.secondary,
            },
            { text: "User", color: config.colors.commands.primary },
            {
              text: `${os.userInfo().username}`,
              color: config.colors.commands.secondary,
            },
            {
              text: "Working Directory",
              color: config.colors.commands.primary,
            },
            {
              text: `${require.main.path}`,
              color: config.colors.commands.secondary,
            },
            { text: "Arch", color: config.colors.commands.primary },
            { text: `${os.arch()}`, color: config.colors.commands.secondary },
            { text: "OS", color: config.colors.commands.primary },
            { text: `${os.platform}`, color: config.colors.commands.secondary },
            { text: "OS Version", color: config.colors.commands.primary },
            {
              text: `${os.version()}`,
              color: config.colors.commands.secondary,
            },
            { text: "Kernel Version", color: config.colors.commands.primary },
            {
              text: `${os.release()}`,
              color: config.colors.commands.secondary,
            },
            { text: "CPU", color: config.colors.commands.primary },
            {
              text: `${os.cpus()[0].model}`,
              color: config.colors.commands.secondary,
            },
            { text: "CPU cores", color: config.colors.commands.primary },
            { text: `${os.cpus().length}`, color: config.colors.integer },
            { text: "Processes", color: config.colors.commands.primary },
            {
              text: `${execSync("ps aux | wc -l").toString().replace("\n", "")}`,
              color: config.colors.integer,
            },
            { text: "Node Version", color: config.colors.commands.primary },
            {
              text: `${process.version}`,
              color: config.colors.commands.secondary,
            },
            { text: "NPM Version", color: config.colors.commands.primary },
            {
              text: `${execSync("npm -v").toString().replaceAll("\n", "")}`,
              color: config.colors.commands.secondary,
            },
          ],
        });
        break;
      case "version":
      case "ver":
        if (botInfo.buildstring.codename.length > 0) {
          component.push({
            translate: "%s %s %s-%s-%s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s-%s",
            color: config.colors.commands.tertiary,
            with: [
              { text: "Friday Night Funkin", color: "dark_blue" },
              { text: "Boyfriend", color: "dark_aqua" },
              { text: "Bot", color: "blue" },
              {
                text: `${botInfo.buildstring.version}`,
                color: config.colors.integer,
              },
              botInfo.buildstring.codename,
              { text: "Build", color: config.colors.commands.primary },
              {
                text: `${botInfo.buildstring.build}`,
                color: config.colors.integer,
              },
              { text: "Repo Build", color: config.colors.commands.primary },
              {
                text: `${execSync("git rev-list --count --all").toString().replaceAll("\n", "")}`,
                color: config.colors.integer,
              },
              {
                text: "Version Release Date",
                color: config.colors.commands.primary,
              },
              {
                text: `${new Date(execSync("git log -1 --format=%ci").toString()).toLocaleString("en-US", { timeZone: "America/CHICAGO" })}`,
                color: config.colors.commands.secondary,
              },
              { text: "Commit", color: config.colors.commands.primary },
              {
                text: `${execSync("git rev-parse HEAD").toString().substring(0, 10)}`,
                color: config.colors.commands.secondary,
              },
              { text: "11/22/22", color: config.colors.commands.primary },
              {
                text: `${new Date().toLocaleDateString("en-US", { timeZone: "America/CHICAGO" })}`,
                color: config.colors.commands.secondary,
              },
            ],
          });
        } else {
          component.push({
            translate: "%s %s %s-%s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s-%s",
            color: config.colors.commands.tertiary,
            with: [
              { text: "Friday Night Funkin", color: "dark_blue" },
              { text: "Boyfriend", color: "dark_aqua" },
              { text: "Bot", color: "blue" },
              {
                text: `${botInfo.buildstring.version}`,
                color: config.colors.integer,
              },
              { text: "Build", color: config.colors.commands.primary },
              {
                text: `${botInfo.buildstring.build}`,
                color: config.colors.integer,
              },
              { text: "Repo Build", color: config.colors.commands.primary },
              {
                text: `${execSync("git rev-list --count --all").toString().replaceAll("\n", "")}`,
                color: config.colors.integer,
              },
              {
                text: "Version Release Date",
                color: config.colors.commands.primary,
              },
              {
                text: `${new Date(execSync("git log -1 --format=%ci").toString()).toLocaleString("en-US", { timeZone: "America/CHICAGO" })}`,
                color: config.colors.commands.secondary,
              },
              { text: "Commit", color: config.colors.commands.primary },
              {
                text: `${execSync("git rev-parse HEAD").toString().substring(0, 10)}`,
                color: config.colors.commands.secondary,
              },
              { text: "11/22/22", color: config.colors.commands.primary },
              {
                text: `${new Date().toLocaleDateString("en-US", { timeZone: "America/CHICAGO" })}`,
                color: config.colors.commands.secondary,
              },
            ],
          });
        }
        break;
      default:
        throw new CommandError({
          translate: "command.unknown.argument",
          color: "dark_red",
        });
    }

    if (source?.sources?.console) {
      bot.console.info(bot.getMessageAsPrismarine(component)?.toAnsi());
    } else if (source.sources.discord) {
      bot.discord.message.reply(
        `\`\`\`ansi\n${fixansi(bot.getMessageAsPrismarine(component)?.toAnsi())}\`\`\``,
      );
    } else {
      bot.tellraw("@a", component);
    }
  }
}

module.exports = info;
