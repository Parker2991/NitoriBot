import { Bot } from "../Bot";
const config = require('../../config')

// i may or may not have used v4's command core for a base here
export class CommandCore {
  public area = {
    start: config.core.area.start ?? { x: 0, y: 0, z: 0 },
    end: config.core.area.end ?? { x: 15, y: 0, z: 15 }
  };

  public position: any;

  public itemPosition: any;

  public currentBlockRelative = { x: 0, y: 0, z: 0 };

  public chatRefill () {
    const bot = Bot;
    const pos = this.position;
    const { start, end } = this.area;

    if (!pos) return;

    bot.chat.command(`minecraft:fill ${pos.x + start.x} ${pos.y + start.y} ${pos.z + start.z} ${pos.x + end.x} ${pos.y + end.y} ${pos.z + end.z} command_block`);
  };

  public itemRefill () {
    // will work on this later
  };

  public move (pos = this.position) {
    this.position = {
      x: Math.floor(pos.x / 16) * 16,
      y: 0,
      z: Math.floor(pos.z / 16) * 16
    }

    this.itemPosition = {
      x: Math.floor(pos.x),
      y: Math.floor(pos.y - 1),
      z: Math.floor(pos.z),
    };

    this.chatRefill();
  }

  public useCommandBlock (command: any, location: any, mode: any, flags: any) {
    const bot = Bot
    bot._client.write("update_command_block", {
      command: command?.substring(0, 32767),
      location: location,
      mode: mode,
      flags: flags,
    });
  };

  public currentBlock () {
    const relativePosition = this.currentBlockRelative
    const corePosition = this.position
    if (!corePosition) return null
    return { x: relativePosition.x + corePosition.x, y: relativePosition.y + corePosition.y, z: relativePosition.z + corePosition.z }
  }

  public incrementCurrentBlock () {
    const relativePosition = this.currentBlockRelative
    const { start, end } = this.area

    relativePosition.x++

    if (relativePosition.x > end.x) {
      relativePosition.x = start.x
      relativePosition.z++
    }

    if (relativePosition.z > end.z) {
      relativePosition.z = start.z
      relativePosition.y++
    }

    if (relativePosition.y > end.y) {
      relativePosition.x = start.x
      relativePosition.y = start.y
      relativePosition.z = start.z
    }
  }

  public run (command: any) {
    const bot = Bot
    const location = this.currentBlock()
    if (!location) return

    bot._client.write('update_command_block', { command, location, mode: 1, flags: 0b100 })

    this.incrementCurrentBlock();
  }

  constructor () {
    const bot = Bot
    bot.listener.on('move', () => {
      this.move(bot.position)
      this.run('say meow :3')
    });
  }
}