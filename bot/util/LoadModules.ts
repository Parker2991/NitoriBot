import fs from 'fs';
import path from 'path';

export async function LoadModules (context: any) {
  const bot = context.bot;
  const config = context.config
  for (
    const filename of fs.readdirSync(
      path.join(__dirname, '../modules')
    )
  ) {
    try {
      if (filename.endsWith('.js')) {
        const module = await import(path.join(__dirname, '../modules/', filename));
        new module.default({ bot, config });
        //bot.modules.push(module)
      }
    } catch (e) {
      console.error(`Failed to load module: ${filename} due to the following Error\n`);
      console.error(e)
    }
  }
}
