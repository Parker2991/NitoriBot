// taken from FNFBoyfriendBot v8.0.0

export default class TabComplete {
  constructor(context: any) {
    const bot = context.bot;
    // let aaa cook
    bot.tabcomplete = (str: any) => {
      return new Promise((resolve) => {
        bot._client.write("tab_complete", {
          text: str,
          assumeCommand: false,
          sendBlockInSight: false,
        });
        const tab_completeH = (packet: any) => {
          bot._client.removeListener("tab_complete", tab_completeH);
          resolve(packet.matches);
        };
        bot._client.once("tab_complete", tab_completeH);
      });
    };
  }
}
