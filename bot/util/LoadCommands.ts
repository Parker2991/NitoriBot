import fs from 'fs';
import path from 'path';

export async function LoadCommands (bot: any, config: any) {
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
        }
      } catch (error) {
        console.error("Failed to load command", filename, ":", error);
      }
    }
  }
}
