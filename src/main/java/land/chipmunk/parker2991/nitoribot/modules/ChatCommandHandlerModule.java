package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.Config;
import land.chipmunk.parker2991.nitoribot.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.data.chat.PlayerMessageData;
import land.chipmunk.parker2991.nitoribot.listeners.*;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;

import net.kyori.adventure.text.Component;

import java.util.List;

public class ChatCommandHandlerModule extends Listener {
  private Bot bot;

  public void sendFeedback (Component message, boolean broadcast) {
    bot.core.run("say meow");
  }

  @Override
  public void parsedMessage (Component message, PlayerMessageData data) {
    if (data.chatType != "minecraft:chat") return;

    List<String> prefixes = bot.config.prefixes;

    for (String prefix : prefixes) {
      String plainMessage = ComponentUtil.componentToString(data.contents);

      if (!plainMessage.startsWith(prefix)) return;

      String command = plainMessage.substring(prefix.length());

      CommandSource source = new CommandSource(bot, data.sender);

      Component testMessage = Component.text("hello world!");
      
      bot.commandManager.executeString(source, command);

    }
  }

  public ChatCommandHandlerModule (Bot bot) {
    this.bot = bot;

    bot.ListenerManager.addListener(this);
  }
}