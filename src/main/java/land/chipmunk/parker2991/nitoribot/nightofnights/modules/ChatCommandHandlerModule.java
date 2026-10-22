package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.Bot;
import land.chipmunk.parker2991.nitoribot.nightofnights.Config;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.ParsedChatType;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.chat.PlayerMessageData;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil;
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