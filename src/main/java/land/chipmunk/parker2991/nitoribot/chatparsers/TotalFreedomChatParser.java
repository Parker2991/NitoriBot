package land.chipmunk.parker2991.nitoribot.chatparsers;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.data.chat.ParseChatData;
import land.chipmunk.parker2991.nitoribot.data.chat.PlayerMessageData;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.w3c.dom.Text;

import java.util.List;

public class TotalFreedomChatParser implements ParseChatData {
  private Bot bot;
  public TotalFreedomChatParser (Bot bot) {
    this.bot = bot;
  }
  @Override
  public PlayerMessageData parse (Component message) {
    if (message instanceof TextComponent) return parse((TextComponent) message);
    return null;
  }
/*
  private boolean isSeperatorAt (List<Component> children, int start) {
    return (children.get(start).equals(SEPERATOR_COLON)
    || children.get(start).equals(SEPERATOR_COLON_RACCOON)) && children.get(start + 1).equals(SEPERATOR_SPACE);
  }
 */

  private boolean isSeperatorAt (List<Component> children, int start) {
    return (
      children.get(start).equals(">")
      ||
        children.get(start).equals(">")
      );
  }
  public PlayerMessageData parse (TextComponent message) {
    String content = message.content();
    List<Component> children = message.children();//.toString();

    // parsing total freedom chat is fucking horrid bro

    for (Component component : children) {
      TextComponent textComponent = (TextComponent) component;
      //String e = textComponent.toString().split("[");
    }
    return null;
  }
}