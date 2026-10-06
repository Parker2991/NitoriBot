package land.chipmunk.parker2991.nitoribot.data.chat;

import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;
import net.kyori.adventure.text.Component;

public record PlayerMessageData(
    PlayerProfileData sender,
    Component contents,
    String chatType,
    Component senderName
) {
}