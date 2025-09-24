class register {
  constructor (context) {
    const bot = context.bot
    const options = context.options
    const config = context.config
    const stringMessage = context.stringMessage

    //if (options.mode !== "savageFriends") return
    if (stringMessage === "Please, register to the server with the command: /register <password> <ConfirmPassword>") bot.selfcare.register = true;
    else if (stringMessage === "This user isn't registered!") bot.selfcare.register = true;
    else if (stringMessage === "Successfully registered!") bot.selfcare.register = false;
    else if (stringMessage === "You're already logged in!") bot.selfcare.register = false;
    else if (stringMessage === "Successful login!") bot.selfcare.register = false;
  }
}

module.exports = register;