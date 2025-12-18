import { EventEmitter } from 'events';
import optionss from './options';

import { chat } from './modules/chat';
import { commandHandler } from './modules/commandHandler';
import { Console } from './modules/console';
import { memUsage } from './modules/memUsage';
import { players } from './modules/players';
import { registry } from './modules/registry';
import { selfcare } from './modules/selfcare';
import { tabComplete } from './modules/tabComplete';
import { position } from './modules/position';
import { core } from './modules/core';
import { reconnect } from './modules/reconnect';
import { commandManager } from './modules/commandManager';

export default class FNFBoyfriendBot extends EventEmitter {
  public _client: any
  public bots: any;
  public chat: any;
  public core: any;
  public console: any;
  public entityId: number;
  public modules: any;
  public options: any;
  public players: any;
  public position: any;
  public reconnectDelay: any;
  public registry: any;
  public selfcare: any;
  public tabcomplete: any;
  constructor (options: any, config: any) {
    super();
    const defaultOptions = new optionss()
    this.options = {
      host: options.host ??= defaultOptions.host,
      port: options.port ??= defaultOptions.port,
      username: options.username ??= defaultOptions.username,
      version: options.version ??= defaultOptions.version,
      hideErrors: options.hideErrors ??= defaultOptions.hideErrors,
      scInterval: options.scInterval ??= defaultOptions.scInterval,
      reconnectDelay: options.reconnectDelay ?? defaultOptions.reconnectDelay
    }

    this.options = options;

    new chat({ bot: this, config });
    new Console({ bot: this, config });
    new players({ bot: this, config });
    new registry({ bot: this, config });
    new selfcare({ bot: this, config });
    new tabComplete({ bot: this, config });
    new memUsage({ bot: this, config });
    new reconnect({ bot: this, config });
    new position({ bot: this, config });
    new core({ bot: this, config });
    new commandHandler({ bot: this, config })
  }
}
