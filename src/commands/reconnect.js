module.exports = {
  data: {
    name: "reconnect",
    enabled: true,
    aliases: [
      "end"
    ],
    description: "reconnect to the server",
    usages: [
    ]
  },
  execute (context) {
    const bot = context.bot;
    const args = context.arguments;
    bot._client.end('command reconnect');
  }
}
