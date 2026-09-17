package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.Config;
import land.chipmunk.parker2991.nitoribot.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.data.ParsedChatType;
import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.data.chat.PlayerMessageData;
import land.chipmunk.parker2991.nitoribot.listeners.*;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;

import net.kyori.adventure.text.Component;

import java.util.List;

public class ChatCommandHandlerModule extends Listener {
  private Bot bot;

  @Override
  public void parsedMessage (Component message, PlayerMessageData data) {
    if (data.chatType != "minecraft:chat") return;

    List<String> prefixes = bot.config.prefixes;

    for (String prefix : prefixes) {
      String plainMessage = ComponentUtil.componentToString(data.contents);

      if (!plainMessage.startsWith(prefix)) return;

      String command = plainMessage.substring(prefix.length());

      CommandSource source = new CommandSource(bot, ParsedChatType.NORMAL, data.sender);

      bot.commandManager.executeString(source, command);

    }
  }

  @Override
  public void commandSpyReceived (PlayerProfileData player, String message) {
    List<String> prefixes = bot.config.prefixes;

    for (String prefix : prefixes) {
      if (!message.startsWith(prefix)) return;

      String command = message.substring(prefix.length());

      CommandSource source = new CommandSource(bot, ParsedChatType.COMMANDSPY, player);

      bot.commandManager.executeString(source, command);
    }
  }

  public ChatCommandHandlerModule (Bot bot) {
    this.bot = bot;

    bot.listenerManager.addListener(this);
  }
}