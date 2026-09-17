package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.logger.Logger;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;
import land.chipmunk.parker2991.nitoribot.listeners.*;

import net.kyori.adventure.text.Component;

public class LoggingModule extends Listener {
  private Bot bot;

  @Override
  public void playerChatReceived (Component message) {
    handleMessages(message);
  };

  @Override
  public void disguisedChatReceived (Component message) {
    handleMessages(message);
  }

  @Override
  public void systemChatReceived (Component message) {
    handleMessages(message);
  }

  public void handleMessages (Component _message) {
    final String host = bot.options.host;
    
    final int port = bot.options.port;
    
    String message = ComponentUtil.componentToAnsi(_message);
    //if (host.equals("kaboom.pw")) return;
    Logger.LOG(bot, message);
  }

  public LoggingModule (Bot bot) {
    this.bot = bot;

    bot.listenerManager.addListener(this);
  }
}