import { Bot } from '../Bot';

export class Tellraw {
  public tellraw (selector: any, ...message: any): void {
    Bot.core.run(`minecraft:tellraw ${selector} ` + JSON.stringify(message));
  }
}