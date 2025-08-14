class usernameSelfcare {
  constructor (context) {
    const bot = context.bot
    const options = context.options
    const stringMessage = context.stringMessage
    const config = context.config

    if (stringMessage === `Successfully set your username to "${bot._client.username}"`) {
      bot.selfcare.username = false
      return
    }
    else if (stringMessage?.startsWith("Successfully set your username to ")) bot.selfcare.username = true
    else if (stringMessage === `You already have the username "${bot._client.username}"`) bot.selfcare.username = false
  }
}

module.exports = usernameSelfcare