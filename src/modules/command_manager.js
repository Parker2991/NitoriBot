const fs = require('fs')
const path = require('path')

module.exports = {
  data: {
    enabled: true,
    name: "command manager",
    type: "commands"
  },
  inject (context) {
    const bot = context.bot;
    const config = context.config;
    bot.commandManager = {
      commands: {},
      commandlist: [],
      execute (commandName, args) {
        const command = bot.commandManager.getCommand(commandName)//.toLowerCase()

        try {
          if (!command || !command.execute) console.error('unknown command');
          return command?.execute({ bot, config, arguments: args })
        } catch (error) {
          console.error(error)
        }
      },

      executeString (command) {
        const [commandName, ...args] = command.split(' ')
        return this.execute(commandName, args)
      },

      register (command) {
        this.commands[command.data.name] = command
        if (command.data.aliases) {
          command.data.aliases.map((a) => (
            this.commands[a] = command
          ))
        }
      },

      getCommand (name) {
        return this.commands[name]
      },

      getCommands () {
        return Object.values(this.commands)
      }
    }

    for (const filename of fs.readdirSync(path.join(__dirname, '../commands'))) {
      try {
        const command = require(path.join(__dirname, '../commands', filename))
        bot.commandManager.register(command);
        bot.commandManager.commandlist.push(command);
      } catch (error) {
        console.error('Failed to load command', filename, ':', error)
      }
    }
  }
}

