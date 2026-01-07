import fs from 'fs';
import path from 'path';

export async function loadCommands (bot: any, config: any) {
  const foldersPath = path.join(__dirname, "../commands");
  const commandFolders = fs.readdirSync(foldersPath);
  for (const folder of commandFolders) {
    const commandsPath = path.join(foldersPath, folder);
    const commandFiles = fs.readdirSync(commandsPath);
    for (const filename of commandFiles) {
      try {
        const filePath = path.join(commandsPath, filename);

        if (filename.endsWith(".js")) {
          const folderName = path.basename(path.dirname(filePath));
          const commands = await import(filePath);
          const command = new commands.default();
          bot.commandManager.register(command, filename, folderName);
          bot.commandManager.list.push(command);
          //console.log(command)
          //console.log(new commands.default())
          //const command = new commands.default();
          //console.log(command)
       //   bot.commandManager.register(command, filename, folderName);
//          console.log(command.default)
         // console.log(path.basename(path.dirname(filePath)));
//          console.log(path.dirname(commandFolders))
        //  console.log(path.dirname(filename).split(filePath))
         // console.log(path.dirname(filename))
//          console.log(filename);
      // path.dirname(filename).split(path.sep).pop()
//          console.log(commandsPath)
        }
      } catch (error) {
        console.error("Failed to load command", filename, ":", error);
      }
    }
  }
}