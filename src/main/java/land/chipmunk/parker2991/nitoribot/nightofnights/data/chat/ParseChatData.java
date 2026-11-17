package land.chipmunk.parker2991.nitoribot.nightofnights.data.chat;

import net.kyori.adventure.text.Component;

public interface ParseChatData {
    PlayerMessageData parse (
        ChatType chatType,
        Component message
    );
}
