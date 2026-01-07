import { CommandContext } from '../../command/CommandContext';

export default class test extends CommandContext {
  constructor() {
    super(
      ['te'],
      "testing stuff",
      []
    )
  }

  execute (context: any) {
    const bot = context.bot;
    const config = context.config;
    const args = context.arguments;
    const Item = require("prismarine-item")(bot.options.version);
    //console.log(Item)
    let position = { x: bot.position.x, y: bot.position.y - 1, z: bot.position.z }

    console.log(Item.toNotch( new Item( bot.registry.itemsByName.oak_sign.id, 1, 0, ), ),)
    bot._client.write('set_creative_slot', {
      slot: 36,
      item: Item.toNotch(
        new Item(
          bot.registry.itemsByName.oak_sign.id,
          1,
          0,
        ),
      ),
    });
    
    bot._client.write('block_dig', {
      status: 0,
      location: position,
      face: 0
    });

    bot._client.write('block_place', {
      hand: 0,
      location: position,
      direction: 0,
      cursorX: 0.1,
      cursorY: 0,
      cursorZ: 0.1,
      insideBlock: false
    });
  }
}
