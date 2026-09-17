package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.listeners.Listener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class CommandSpyModule extends Listener {
  private Bot bot;

  private static final String COLON = ":";

  private static final String SLASH = "/";

  @Override
  public void systemChatReceived (Component data) {
    @Unmodifiable List<Component> children = data.children();

    TextComponent userComponent = null;

    TextComponent commandComponent = null;

    if (children.get(1) instanceof final TextComponent textComponent) commandComponent = textComponent;

    if (data instanceof final TextComponent textComponent) userComponent = textComponent;

    PlayerProfileData sender = null;

    for (PlayerProfileData player : bot.players.list) {
      String username = player.profile.getName();

      if (username.equals(userComponent.content())) sender = player;
    }

    String command = commandComponent.content().substring(SLASH.length());

    if (
      (
        commandComponent.content().contains(SLASH + command)
          &&
          sender != null
      )
    ) {
      for (Listener listener : bot.listenerManager.listeners) {
        listener.commandSpyReceived(sender, command);
      }
    }
  }

  public CommandSpyModule (Bot bot) {
    this.bot = bot;

    bot.listenerManager.addListener(this);
  }
}
