import fs from "fs";
import path from "path";

export default async function loadModules (bot: any, config: any) {
  for (const filename of fs.readdirSync(path.join(__dirname, '../modules'))) {
    try {
      if (filename.endsWith(".ts") || filename.endsWith(".js")) {
        const module = await import(path.join(__dirname, '../modules/', filename))
        const modules = module.default;
        new modules({ bot, config })
      }
    } catch (error: unknown) {
      if (error instanceof Error) {
        console.error(`Failed to load module ${filename} due to error`);
        console.error(`\x1b[31m${error.stack}\x1b[0m`);
      }
    }
  } 
}