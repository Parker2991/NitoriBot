// i may or may not have used v4's command core for a base here

export class core {
  constructor (context: any) {
    const bot = context.bot;
    const config = context.config;
    const options = bot.options;

    bot.core = {
      area: {
        start: options.core?.area.start ?? { x: 0, y: 0, z: 0 },
        end: options.core?.area.end ?? { x: 15, y: 0, z: 15 }
      },
      position: null,
      currentBlockRelative: { x: 0, y: 0, z: 0 },

      refill () {
        const pos = bot.core.position
        const { start, end } = bot.core.area

        if (!pos) return

        bot.chat.command(`fill ${pos.x + start.x} ${pos.y + start.y} ${pos.z + start.z} ${pos.x + end.x} ${pos.y + end.y} ${pos.z + end.z} repeating_command_block`)
      },

      move (pos = bot.position) {
        bot.core.position = {
          x: Math.floor(pos.x / 16) * 16,
          y: 0,
          z: Math.floor(pos.z / 16) * 16
        }
        bot.core.refill()
      },

      currentBlock () {
        const relativePosition = bot.core.currentBlockRelative
        const corePosition = bot.core.position
        if (!corePosition) return null
        return { x: relativePosition.x + corePosition.x, y: relativePosition.y + corePosition.y, z: relativePosition.z + corePosition.z }
      },

      incrementCurrentBlock () {
        const relativePosition = bot.core.currentBlockRelative
        const { start, end } = bot.core.area

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
      },

      run (command: any) {
        const location = bot.core.currentBlock()
        if (!location) return

        bot._client.write('update_command_block', { command, location, mode: 1, flags: 0b100 })

        bot.core.incrementCurrentBlock()
      }
    }

    bot.on('move', () => {
      bot.core.move(bot.position)
    })
  }
}