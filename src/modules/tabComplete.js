class tabComplete {
  constructor(context) {
    const bot = context.bot;
    // let aaa cook
    bot.tab_complete = (str) => {
      return new Promise((resolve) => {
        bot._client.write("tab_complete", {
          text: str,
          assumeCommand: false,
          sendBlockInSight: false,
        });
        const tab_completeH = (packet) => {
          bot._client.removeListener("tab_complete", tab_completeH);
          resolve(packet.matches);
        };
        bot._client.once("tab_complete", tab_completeH);
      });
    };
  }
}
module.exports = tabComplete;
