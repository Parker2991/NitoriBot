package land.chipmunk.parker2991.nitoribot.command;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.data.ParsedChatType;
import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;
import net.kyori.adventure.text.Component;

public class CommandSource {
    private final Bot bot;

    public final PlayerProfileData sender;

    public boolean inGame;

    public final ParsedChatType parsedChatType;

    public CommandSource (Bot bot, ParsedChatType parsedChatType, PlayerProfileData sender) {
        this.bot = bot;
        this.parsedChatType = parsedChatType;
        this.sender = sender;
    }

    public void sendFeedback (Component message) {
        String convertToJson = ComponentUtil.componentToJSON(message);
        //Vector3d playerPos = sender.position.position();

   /* String formatCommand = String.format(
      "minecraft:summon text_display %s %s %s {tag:[%s], text:%s}",
      playerPos.getX(),
      playerPos.getY(),
      playerPos.getZ(),
      "NitoriBot",
      convertToJson
    );*/

        String selector = String.format(
            "@p[nbt={UUID:%s}]",
            sender.entityUUID()
        );

        //System.out.println(sender.profile.getName());

        if (this.parsedChatType == ParsedChatType.NORMAL) bot.chat.tellraw("@a", message);
        else bot.chat.tellraw(selector, message);
    }

}
