import { EventEmitter } from 'events';
import optionss from './Options';
import { LoadModules } from './util/LoadModules';

export default class FNFBoyfriendBot extends EventEmitter {
  public _client: any;
  public bots: any;
  public chat: any;
  public commandManager: any;
  public core: any;
  public console: any;
  public entityId: number;
  public modules: [];
  public options: any;
  public players: any;
  public position: any;
  public reconnectDelay: any;
  public registry: any;
  public selfcare: any;
  public tabcomplete: any;
  public world: any;

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

    LoadModules({ bot: this, config: config })
  }
}
