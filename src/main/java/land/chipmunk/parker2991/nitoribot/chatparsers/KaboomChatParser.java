package land.chipmunk.parker2991.nitoribot.chatparsers;

import java.util.List;

import land.chipmunk.parker2991.nitoribot.data.chat.*;
import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class KaboomChatParser implements ParseChatData {
  private Bot bot;
  private static Component SEPERATOR_COLON = Component.text(":");
  private static Component SEPERATOR_COLON_RACCOON = Component.text("\u00a7f:"); // https://github.com/raccoonserver/extras/commit/315f704075db2f00e3e1bfbf55f858469c22880f
  private static Component SEPERATOR_SPACE = Component.space();

  public KaboomChatParser (Bot bot) {
    this.bot = bot;
  }

  @Override
  public PlayerMessageData parse (Component message) {
    if (message instanceof TextComponent) return parse((TextComponent) message);
    return null;
  }

  public PlayerMessageData parse (TextComponent message) {
    List<Component> children = message.children();

    if (!message.content().equals("") || !message.style().isEmpty() || children == null || children.size() < 3) return null;

    final Component prefix = children.get(0);
    Component displayName = Component.empty();
    Component contents = Component.empty();

    if (isSeperatorAt(children, 1)) { // Missing/blank display name
      if (children.size() > 3) contents = children.get(3);
    } else if (isSeperatorAt(children, 2)) {
      displayName = children.get(1);
      if (children.size() > 4) contents = children.get(4);
    } else {
      return null;
    }

    PlayerProfileData sender = bot.players.getDisplayName(Component.empty().append(prefix).append(displayName));//getSender());
    return new PlayerMessageData(sender, contents, "minecraft:chat", displayName);
  }

  private boolean isSeperatorAt (List<Component> children, int start) {
    return (children.get(start).equals(SEPERATOR_COLON) || children.get(start).equals(SEPERATOR_COLON_RACCOON)) && children.get(start + 1).equals(SEPERATOR_SPACE);
  }
}