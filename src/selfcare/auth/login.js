class loginSelfcare {
  constructor (context) {
    const bot = context.bot
    const options = context.options
    const config = context.config
    const stringMessage = context.stringMessage

    //if (options.mode !== "savageFriends") return

    if (stringMessage === "Please, login with the command: /login <password>") bot.selfcare.login = true;
    else if (stringMessage === "Successful login!") bot.selfcare.logged = false;
    else if (stringMessage === "You're already logged in!") bot.selfcare.login = false;
  }
}

module.exports = loginSelfcare