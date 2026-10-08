package land.chipmunk.parker2991.nitoribot.chatparsers;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.data.chat.ParseChatData;
import land.chipmunk.parker2991.nitoribot.data.chat.PlayerMessageData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

public class KaboomChatParser implements ParseChatData {
    private static final Component SEPERATOR_COLON = Component.text(":");
    private static final Component SEPERATOR_COLON_RACCOON = Component.text("§f:"); // https://github.com/raccoonserver/extras/commit/315f704075db2f00e3e1bfbf55f858469c22880f
    private static final Component SEPERATOR_SPACE = Component.space();
    private final Bot bot;

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

        if (!message.content().isEmpty() || !message.style().isEmpty() || children.size() < 3) return null;

        final Component prefix = children.getFirst();
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

        PlayerProfileData sender = bot.players.getDisplayName(Component.empty().append(prefix).append(displayName));
        return new PlayerMessageData(sender, contents, "minecraft:chat", displayName);
    }

    private boolean isSeperatorAt (List<Component> children, int start) {
        return (children.get(start).equals(SEPERATOR_COLON) || children.get(start).equals(SEPERATOR_COLON_RACCOON)) && children.get(
            start + 1).equals(SEPERATOR_SPACE);
    }
}