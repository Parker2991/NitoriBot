package land.chipmunk.parker2991.nitoribot.data.chat;

import net.kyori.adventure.text.Component;

public interface ParseChatData {
  PlayerMessageData parse (Component message);
}
