package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.logger.LoggerManager;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;
import land.chipmunk.parker2991.nitoribot.listeners.*;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ConsoleModule extends Listener {
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

  public void handleMessages (Component message) {
    final String host = bot.options.host;
    final int port = bot.options.port;
    String parsed = ComponentUtil.componentToAnsi(message);
    Component component = Component.text(host + ":" + port).color(NamedTextColor.BLUE);
    LoggerManager.LOG(component, parsed);

  }

  public ConsoleModule (Bot bot) {
    this.bot = bot;

    bot.ListenerManager.addListener(this);
  }
}