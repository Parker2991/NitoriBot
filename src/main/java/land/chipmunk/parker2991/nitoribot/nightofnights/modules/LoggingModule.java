package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.chat.ChatType;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingModule implements Listener {
    private final NitoriBot bot;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingModule.class);

    public LoggingModule (NitoriBot bot) {
        this.bot = bot;

        bot.listenerManager.addListener(this);
    }

    private Component chatTypes (
        ChatType chatType,
        Component component
    ) {
        return switch (chatType) {
            case ChatType.PLAYER -> Component.translatable(
                "[P] %s",
                component
            );
            case ChatType.DISGUISED -> Component.translatable(
                "[D] %s",
                component
            );
            case ChatType.SYSTEM -> Component.translatable(
                "[S] %s",
                component
            );
        };
    }

    @Override
    public void chatMessageReceived (
        ChatType chatType,
        Component component
    ) {
        final Component message = chatTypes(
            chatType,
            component
        );
        handleMessages(message);
    }

    public void handleMessages (Component _message) {
        Component host = Component
            .text(String.format(
                "[%s:%s] ",
                bot.options.host,
                bot.options.port
            ))
            .color(NamedTextColor.BLUE);
        Component component = Component
            .empty()
            .append(host)
            .append(_message);
        String message = ComponentUtil.componentToAnsi(component);

        bot.logger.log(message);
        //LOGGER.info(message);
    }
}