import { Bot } from '../Bot';
const ChatMessage = require('prismarine-chat')('1.21.9');
let queueCount = 0;
import version from '../resources/version.json';

export class IRC {
  private handleMessages (message: any) {

  }

  private send (message: any) {
    const bot = Bot;
    queueCount++
    const client = bot.ircClient;

    const string = ChatMessage.fromNotch(message.message)?.toString().substring(0, 512);

    const channel = client.channel(bot.options.ircChannel);
    
    if (queueCount > 4) return
    else channel.say(string)
  }

  constructor () {
    const bot = Bot;
    const client = bot.ircClient;
    const options = bot.options;
    const botCodename = ChatMessage.fromNotch(version.bot.codename)?.toString();
    const fouCodename = ChatMessage.fromNotch(version.foundation.codename)?.toString();

    client.on('registered', (data: any) => {
      client.join(options.ircChannel);
      const channel = client.channel(options.ircChannel);
      channel.say(`FNFBoyfriendBot ${version.bot.version}-${botCodename} Build: ${version.bot.build}`);
      channel.say(`${fouCodename} Foundation ${version.foundation.version} Build: ${version.foundation.build}`);
    });

    client.on('whois', (data: any) => {

    })

    client.on('join', (data: any) => {

    })

    bot.listener.on('message', this.send)

    client.on('message', (data: any) => {
      const user = data.nick;
      const message = data.message;

      client.who(options.ircChannel)

      client.on('wholist', (dataa: any) => {
        const users = dataa.users;
        const findUser = users.find((a: any) => a.nick === user);
        console.log(findUser);
      });

      let component = [];

      component.push(
        {
          translate: "[%s] %s \u203a %s",
          with: [
            {
              translate: "%s%s%s %s",
              bold: false,
              with: [
                {
                  text: "FNF",
                  bold: false,
                  color: "blue",
                },
                {
                  text: "Boyfriend",
                  bold: false,
                  color: "dark_aqua",
                },
                {
                  text: "Bot",
                  bold: false,
                  color: "dark_blue",
                },
                {
                  text: "IRC",
                  bold: false,
                  color: "blue"
                }
              ],
            },
            {
              translate: "<%s>",
              color: "blue",
              bold: false,
              with: [
                {
                  text: `${user}`,
                  bold: false,
                  color: "dark_blue",
                }
              ]
            },
            message
          ]
        }
      ) // component totally wasnt taking from Reignite's discord module :tm:

      bot.tellraw("@a", component);
    });

    setInterval(() => {
      queueCount = 0;
    }, 3000)
  }
}
