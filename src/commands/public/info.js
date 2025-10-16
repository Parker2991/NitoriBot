const os = require("os");
const CommandError = require("../../commandUtil/errors/CommandError");
const botInfo = require("../../data/info.json");
const { execSync } = require("child_process");
const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");

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
        "config",
        "contributors/credits",
        "discord",
        "loaded",
        "usages ",
        "uptimes",
        "server",
        "version",
      ],
    );
  }

  execute(context) {
    const bot = context.bot;
    const args = context.arguments;
    const config = context.config;
    const discordClient = context.discordClient;
    const source = context.source;
    const translations = bot.translations;
    let component = [];

    switch (args[0]?.toLowerCase()) {
      case "about":
        component.push({
          translate: "fnfboyfriendbot.command.info.about",
          fallback: translations["fnfboyfriendbot.command.info.about"],
          color: config.colors.commands.primary,
          with: [
            { text: `${botInfo.buildstring.url}`, color: config.colors.commands.secondary }
          ],
          hover_event: {
            action: "show_text",
            value: [
              {
                text: "click here to view bots source code",
                color: `${config.colors.commands.primary}`,
              },
            ],
          },
          click_event: {
            action: "open_url",
            url: `${botInfo.buildstring.url}`,
          }
        })
      break;
      case "config":
        component.push({
          translate: "%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s",
          color: config.colors.commands.tertiary,
          with: [
            {
              translate: "fnfboyfriendbot.command.info.config.server.ip",
              fallback: translations["fnfboyfriendbot.command.info.config.server.ip"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${bot.options.host}`, color: config.colors.commands.primary },
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${bot.options.port}`, color: config.colors.integer }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.config.server.name",
              fallback: translations["fnfboyfriendbot.command.info.config.server.name"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                bot.options.serverName
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.config.minecraft.username",
              fallback: translations["fnfboyfriendbot.command.info.config.minecraft.username"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${bot._client.username}`, color: config.colors.commands.secondary }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.config.minecraft.version",
              fallback: translations["fnfboyfriendbot.command.info.config.minecraft.version"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${bot._client.version}`, color: config.colors.commands.secondary },
              ] 
            },
            {
              translate: "fnfboyfriendbot.command.info.config.discord.username",
              fallback: translations["fnfboyfriendbot.command.info.config.discord.username"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${discordClient?.user?.tag}`, color: config.colors.commands.secondary }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.config.discord.channel",
              fallback: translations["fnfboyfriendbot.command.info.config.discord.channel"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${bot.discord?.channel?.name}`, color: config.colors.commands.secondary }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.config.server.count",
              fallback: translations["fnfboyfriendbot.command.info.config.server.count"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${bot.bots.length}`, color: config.colors.integer }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.config.prefixes",
              fallback: translations["fnfboyfriendbot.command.info.config.prefixes"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${config.prefixes.map((e) => e + " ").join(' ')}`, color: config.colors.commands.secondary }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.config.mode",
              fallback: translations["fnfboyfriendbot.command.info.config.mode"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${bot.options.mode}`, color: config.colors.commands.secondary }
              ]
            }
          ]
        })
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
          translate: "fnfboyfriendbot.command.info.discord",
          fallback: translations["fnfboyfriendbot.command.info.discord"],
          color: config.colors.commands.primary,
          with: [
            { text: `${config.discord.invite}`, color: config.colors.commands.secondary }
          ],
          hover_event: {
            action: "show_text",
            value: [
              {
                text: "click here to join the discord server!",
                color: `${config.colors.commands.primary}`,
              },
            ],
          },
          click_event: {
            action: "open_url",
            url: `${config.discord.invite}`,
          }
        })
      break;
      case "loaded":
        component.push({
          translate: "%s\n%s",
          with: [
            {
              translate: "fnfboyfriendbot.command.info.loaded.commands",
              fallback: translations["fnfboyfriendbot.command.info.loaded.commands"],
              color: config.colors.commands.primary,
              with: [
                { text: `${bot.commandManager.commandlist.length}`, color: config.colors.integer}
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.loaded.modules",
              fallback: translations["fnfboyfriendbot.command.info.loaded.modules"],
              color: config.colors.commands.primary,
              with: [
                { text: `${bot.modules.length}`, color: config.colors.integer }
              ]
            }
          ]
        })
      break;
      case "server":
        component.push({
          translate: "%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s",
          color: config.colors.commands.primary,
          with: [
            { 
              translate: "fnfboyfriendbot.command.info.server.hostname",
              fallback: translations["fnfboyfriendbot.command.info.server.hostname"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${os.hostname()}`, color: config.colors.commands.secondary },
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.server.user",
              fallback: translations["fnfboyfriendbot.command.info.server.user"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${os.userInfo().username}`, color: config.colors.commands.secondary }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.server.directory",
              fallback: translations["fnfboyfriendbot.command.info.server.directory"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${require.main.path}`, color: config.colors.commands.secondary }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.server.arch",
              fallback: translations["fnfboyfriendbot.command.info.server.arch"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${os.arch}`, color: config.colors.commands.secondary }
              ]
            },
            { 
              translate: "fnfboyfriendbot.command.info.server.os",
              fallback: translations["fnfboyfriendbot.command.info.server.os"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${os.platform}`, color: config.colors.commands.primary }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.server.os.version",
              fallback: translations["fnfboyfriendbot.command.info.server.os.version"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${os.version()}`, color: config.colors.commands.secondary }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.server.kernel_version",
              fallback: translations["fnfboyfriendbot.command.info.server.kernel_version"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${os.release()}`, color: config.colors.commands.secondary }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.server.cpu",
              fallback: translations["fnfboyfriendbot.command.info.server.cpu"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${os.cpus()[0].model}`, color: config.colors.commands.secondary }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.server.cpu.cores",
              fallback: translations["fnfboyfriendbot.command.info.server.cpu.cores"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${os.cpus().length}`, color: config.colors.integer }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.server.processes",
              fallback: translations["fnfboyfriendbot.command.info.server.processes"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${execSync("ps aux | wc -l").toString().replace("\n", "")}`, color: config.colors.integer }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.server.node_version",
              fallback: translations["fnfboyfriendbot.command.info.server.node_version"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${process.version}`, color: config.colors.commands.secondary }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.server.npm_version",
              fallback: translations["fnfboyfriendbot.command.info.server.npm_version"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${execSync("npm -v").toString().replaceAll("\n", "")}`, color: config.colors.commands.secondary }
              ]
            }
          ]
        })
      break;
      case "usages":
       component.push({
          translate: "%s\n%s",
          color: config.colors.commands.tertiary,
          with: [
            {
              translate: "fnfboyfriendbot.command.info.usages.botmem",
              fallback: translations["fnfboyfriendbot.command.info.usages.botmem"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${Math.floor(process.memoryUsage().heapUsed / 1048576)}`, color: config.colors.integer },
                { text: `${Math.floor(process.memoryUsage().heapTotal / 1048576)}`, color: config.colors.integer }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.usages.servermem",
              fallback: translations["fnfboyfriendbot.command.info.usages.servermem"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${Math.floor(os.totalmem() / 1048576) - Math.floor(os.freemem() / 1048576)}`, color: config.colors.integer },
                { text: `${Math.floor(os.totalmem() / 1048576)}`, color: config.colors.integer }
              ]
            }
          ]
        });      
      break;
      case "uptimes":
        component.push({
          translate: "%s\n%s",
          color: config.colors.commands.tertiary,
          with: [
            {
              translate: "fnfboyfriendbot.command.info.uptimes.bot",
              fallback: translations["fnfboyfriendbot.command.info.uptimes.bot"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${format(process.uptime())}`}
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.uptimes.server",
              fallback: translations["fnfboyfriendbot.command.info.uptimes.server"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${format(os.uptime())}`, color: config.colors.commands.secondary }
              ]
            }
          ],
        });
      break;
      case "version":
        component.push({
          translate: "%s %s %s-%s-%s\n%s\n%s\n%s\n%s\n%s-%s",
          color: config.colors.commands.tertiary,
          with: [
            { text: "Friday Night Funkin", color: "dark_blue" },
            { text: "Boyfriend", color: "dark_aqua" },
            { text: "Bot", color: "blue" },
            {
              text: `${botInfo.buildstring.version}`,
              color: config.colors.integer,
            },
            botInfo.buildstring.codename || '',
            {
              translate: "fnfboyfriendbot.command.info.version.build",
              fallback: translations["fnfboyfriendbot.command.info.version.build"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${botInfo.buildstring.build}`, color: config.colors.integer }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.version.repo_build",
              fallback: translations["fnfboyfriendbot.command.info.version.repo_build"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${execSync("git rev-list --count --all").toString().replaceAll("\n", "")}`, color: config.colors.integer }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.version.version_release_date",
              fallback: translations["fnfboyfriendbot.command.info.version.version_release_date"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${new Date(execSync("git log -1 --format=%ci").toString()).toLocaleString("en-US", { timeZone: "America/CHICAGO" })}`, color: config.colors.commands.secondary }
              ]
            },
            {
              translate: "fnfboyfriendbot.command.info.version.commit",
              fallback: translations["fnfboyfriendbot.command.info.version.commit"],
              color: config.colors.commands.primary,
              with: [
                { text: ":", color: config.colors.commands.tertiary },
                { text: `${execSync("git rev-parse HEAD").toString().substring(0, 10)}`, color: config.colors.commands.secondary }
              ]
            },
            { text: "11/22/22", color: config.colors.commands.primary },
            {
              text: `${new Date().toLocaleDateString("en-US", { timeZone: "America/CHICAGO" })}`,
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

    source.sendFeedback(component);
  }
}

module.exports = info;
