import { readdirSync } from "fs";
import { join } from "path";

class loadModules {
  constructor (bot, config) {
    for (const filename of readdirSync(join(__dirname, '../modules'))) {
      try {
        if (filename.endsWith(".ts")) {
          //import * as module from filename;

          const module = import(filename);
          console.log(module)
          //module({ bot, config })
        }
      } catch (error) {
        console.error(`Failed to load module ${filename} due to error`);
        console.error(`\x1b[31m${error.stack}\x1b[0m`);
      }
    } 
  }
}
export = loadModules;