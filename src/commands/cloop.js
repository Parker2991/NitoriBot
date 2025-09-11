module.exports = {
  data: {
    name: "cloop",
    aliases: [
      "commandloop",
      "loop"
    ],
    description: "loops commands",
    enabled: true,
    usages: [

    ]
  },
  execute (context) {
    const bot = context.bot;
    const config = context.config;
    const args = context.arguments;

    if (!args && !args[0] && !args[1] && !args[2] && !args[3] && !args[4] && !args[5]) return;

    switch (args[0]?.toLowerCase()) {
      case "add":
        if (isNaN(args[1]) === true) throw new CommandError("invalid interval");

        const interval = parseInt(args[1]);
        const command = args.slice(2).join(' ');
        bot.cloop.add(command, interval);

        bot.console.command(`added ${command} to the cloops with the interval ${interval}`);
      break;
      case "clear":
        bot.cloop.clear();
        bot.console.command('cleared the cloops');
      break;
      case "remove":
  //      if (isNaN(args[1]) === true) throw new CommandError('argument must be an integer!');

        const index = parseInt(args[1]);
        bot.cloop.remove(index);

        bot.chat.message(`removed ${index} from cloops`);
      break;
      case "list":
        const component = []

        const listComponent = []
        let i = 0
        for (const cloop of bot.cloop.list) {
          listComponent.push({
            translate: '%s \u203a %s (%s)',
            color: "dark_blue",
            with: [
              { text: `${i}`, color: "gold" },
              cloop.command,
              { text: `${cloop.interval}`, color: "gold" },
            ]
          })
          listComponent.push('\n')
          i++
        }

        listComponent.pop()

        component.push({
          translate: 'Cloops (%s):',
          color: "dark_blue",
          with: [
            { text: `${bot.cloop.list.length}`, color: "gold"}
          ]
        })
        component.push('\n')
        
        if (bot.cloop.list.length > 0) {
          component.push(listComponent)
        }
        bot.console.command(component);
        /*
        if (bot.cloop.list.length === 0) {
          bot.tellraw(`@a[name="${source?.player.profile?.name}"]`, {
            translate: 'Cloops (%s):',
            color: config.colors.commands.primary,
            with: [
              { text: `${bot.cloop.list.length}`, color: config.colors. }
            ]
          })
        } else {
          bot.tellraw(`@a[name="${source?.player?.profile?.name}"]`, component)
        }*/
      break;
      default:
        bot.console.command("invalid argument")
        //throw new CommandError('invalid argument')
    }
  }
}
