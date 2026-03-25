package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.logger.LoggerManager;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ConsoleModule extends ChatModule.Listener {
  private Bot bot;

  @Override
  public boolean playerChatReceived (Component message) {
    handleMessages(message);
    return true;
  };

  @Override
  public boolean disguisedChatReceived (Component message) {
    handleMessages(message);
    return true;
  }

  @Override
  public boolean systemChatReceived (Component message) {
    handleMessages(message);
    return true;
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

    bot.chat.addListener(this);
  }
}