const CommandError = require('./command_error');
module.exports = (bot, command, args, source, config) => {
  const event = bot.discord.message;
  const roles = event?.member?.roles?.cache;
  switch (command?.data?.trustLevel) {
    case 0:
      // do nothing since trust level 0 is public
    break;
    case 1:
      if (source?.sources?.discord) {
        const hasRole = roles?.some(role => role.name === `${config.discord.roles.trusted}` || role.name === `${config.discord.roles.admin}` || role.name === `${config.discord.roles.fullAccess}` || role.name === `${config.discord.roles.owner}`)
        if (!hasRole) throw new CommandError({ translate: 'You dont have the trusted, admin, or owner role!', color: "dark_red" })
      } else if (!source?.sources.console) {
        if (args.length === 0) throw new CommandError({ text: "Please provide a trusted, admin or owner hash", color: "dark_red" });
        if (args[0] !== bot.validation.trusted && args[0] !== bot.validation.admin && args[0] !== bot.validation.owner) throw new CommandError({ translate: 'Invalid trusted, admin or owner hash', color: 'dark_red' });
      }
    break;
    case 2:
      if (source?.sources?.discord) {
        const hasRole = roles?.some(role => role.name === `${config.discord.roles.admin}` || role.name === `${config.discord.roles.fullAccess}` ||role.name === `${config.discord.roles.owner}`)
        if (!hasRole) throw new CommandError({ translate: 'You dont have the admin, or owner role!', color: "dark_red" })
      } else if (!source?.sources?.console) {
        if (args.length === 0) throw new CommandError({ text: "Please provide an admin or owner hash", color: 'dark_red' })
        if (args[0] !== bot.validation.admin && args[0] !== bot.validation.owner) throw new CommandError({ translate: 'Invalid admin or owner hash', color: 'dark_red' });
      }
    break;
    case 3:
      if (source?.sources?.discord) {
        const hasRole = roles?.some(role => role.name === `${config.discord.roles.owner}` || role.name === `${config.discord.roles.fullAccess}`)
        if (!hasRole) throw new CommandError({ translate: 'You do not have the owner role!', color: "dark_red" })
      } else if (!source?.sources?.console) {
        if (args.length === 0 && bot.validation.owner) throw new CommandError({ text: "Please provide an owner hash", color: "dark_red" })
        if (args[0] !== bot.validation.owner) throw new CommandError({ translate: 'Invalid owner hash', color: 'dark_red' })
      }
    break;
    case 4:
      if (!source?.sources?.console) {
        throw new CommandError({ text: 'This command can only be ran via console', color: "dark_red" })
      }
    break;
  }
}
