package land.chipmunk.parker2991.nitoribot.command;


import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.data.ParsedChatType;
import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;
import net.kyori.adventure.text.Component;

public record CommandSource(PlayerProfileData sender, boolean inGame, ParsedChatType chatType) {
    public void sendFeedback (Bot bot, Component message) {
        final String selector = String.format("@p[nbt={UUID:%s}]", sender().entityUUID());

        if (chatType() == ParsedChatType.NORMAL) bot.chat.tellraw("@a", message);
        else bot.chat.tellraw(selector, message);
    }
}
