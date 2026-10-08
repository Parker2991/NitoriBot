package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.Config;
import land.chipmunk.parker2991.nitoribot.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.data.ParsedChatType;
import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.data.chat.PlayerMessageData;
import land.chipmunk.parker2991.nitoribot.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatCommandHandlerModule implements Listener {
    private @NotNull final Bot bot;
    private @NotNull final Config config;

    public ChatCommandHandlerModule (Bot bot) {
        this.bot = bot;
        this.config = bot.config;

        bot.listenerManager.addListener(this);
    }

    @Override
    public void parsedMessage (PlayerMessageData data) {
        if (!data.chatType().equals("minecraft:chat")) return;

        List<String> prefixes = config.prefixes;

        for (String prefix : prefixes) {
            String plainMessage = ComponentUtil.componentToString(data.contents());

            if (!plainMessage.startsWith(prefix)) return;

            String command = plainMessage.substring(prefix.length());

            CommandSource source = new CommandSource(data.sender(), true, ParsedChatType.NORMAL);

            bot.commandManager.executeString(source, command);

        }
    }

    @Override
    public void commandSpyReceived (PlayerProfileData player, String message) {
        List<String> prefixes = config.prefixes;

        for (String prefix : prefixes) {
            if (!message.startsWith(prefix)) return;

            String command = message.substring(prefix.length());

            CommandSource source = new CommandSource(player, true, ParsedChatType.COMMANDSPY);

            bot.commandManager.executeString(source, command);
        }
    }
}