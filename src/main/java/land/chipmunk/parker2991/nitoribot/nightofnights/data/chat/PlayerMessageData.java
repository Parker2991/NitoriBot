package land.chipmunk.parker2991.nitoribot.nightofnights.data.chat;

import land.chipmunk.parker2991.nitoribot.nightofnights.data.ChatType;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.PlayerProfileData;
import net.kyori.adventure.text.Component;

public record PlayerMessageData(PlayerProfileData sender, Component contents, ChatType chatType, Component senderName) {
}