import { LoadCommands } from '../util/LoadCommands';
import { CommandError } from '../command/CommandError';

export default class commandManager {
  constructor (context: any) {
    const bot = context.bot;
    const config = context.config;
    const options = bot.options;
    
    bot.commandManager = {
      commands: {},
      
      list: [],

      execute (source: any, commandName: any, args: any) {
        const command = this.getCommand(commandName.toLowerCase());

        try {
          if (!command) throw new CommandError({
              translate: "%s%s%s %s",
              color: "red",
              with: [
                { translate: "command.unknown.command", color: "red" },
                { text: "\n" },
                { text: `${commandName}`, color: "gray" },
                { translate: "command.context.here", color: "red" }
              ]
            }) 

          return command?.execute({ bot, config, arguments: args })
        } catch (e) {
          if (e instanceof CommandError) bot.tellraw("@a", e._message);
          else {
            bot.console.info(e);
            bot.tellraw("@a",
             {
                translate: "command.failed",
                color: "red",
                hoverEvent: {
                  action: "show_text",
                  contents: String(e),
                },
              }
            )
          }
        }
      },

      executeString (source: any, command: any) {
        const [commandName, ...args] = command.split(" ");

        return this.execute(source, commandName, args);
      },

      register (command: any, filename: any, folderName: any) {
        const commandName = filename.split('.js')[0];
        command.data.name = commandName;

        if (folderName === "public") command.data.trustLevel = [0, "public"];
        if (folderName === "trusted") command.data.trustLevel = [0, "trusted"];
        if (folderName === "admin") command.data.trustLevel = [0, "admin"];
        if (folderName === "owner") command.data.trustLevel = [0, "owner"];
        if (folderName === "console") command.data.trustLevel = [0, "console"];
        else command.data.trustLevel = [5, "unknown"];
        this.commands[command.data.name] = command;
        
        if (command.data.aliases) {
          command.data.aliases.map((aliases: any) => this.commands[aliases] = command)
        }
      },

      getCommand(name: any) {
        return this.commands[name];
      },

      getCommands() {
        return Object.values(this.commands);
      },
    }

    LoadCommands(bot, config);
  }
}
