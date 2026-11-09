package land.chipmunk.parker2991.nitoribot.nightofnights.command;


import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.ChatType;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.ParsedChatType;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.PlayerProfileData;
import static land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil.componentToAnsi;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.kyori.adventure.text.Component;

public record CommandSource(
    PlayerProfileData sender,
    MessageReceivedEvent discordEvent,
    CommandMessageSource source,
    ChatType chatType,
    ParsedChatType parsedChatType
) {
    public void sendFeedback (NitoriBot bot, Component component) {
        switch (source) {
            case CommandMessageSource.INGAME -> {
                final String selector = String.format("@p[nbt={UUID:uuid(\"%s\")}]", sender.uuid);

                if (parsedChatType() == ParsedChatType.COMMANDSPY) bot.chat.tellraw(selector, component);
                else bot.chat.tellraw("@a", component);
            }
            case CommandMessageSource.DISCORD -> {
                final String message = "```ansi\n" + componentToAnsi(component) + "\n```";
                discordEvent.getMessage().reply(message).queue();
            }
            case CommandMessageSource.CONSOLE -> {

            }
        }

    }
}