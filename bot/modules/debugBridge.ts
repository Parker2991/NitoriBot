import { connect } from 'socket.io-client';
import version from '../data/version.json';

export default class debugBridge {
  constructor (context: any) {
    const bot = context.bot;
    const config = context.config;
    const options = bot.options;

    if (!bot.debugEnabled) return;

    bot.debugBridge = {
      client: connect(config.debug.host)
    }
// `Starting FNFBoyfriendBot ${version.codename} ${version.version} Build: ${version.build}`
    const client = bot.debugBridge.client;

    client.on('connect', (socket: any) => {
      bot.console.info('Connected to Debug Bridge');

      bot.emit('version', { version: version.version, codename: version.codename, build: version.build })

      bot.on('disguised', (data: any) => socket.emit('diguised', data));

      bot.on('player', (data: any) => socket.emit('player', data));

      bot.on('system', (data: any) => socket.emit('system', data));
    });
  }
}