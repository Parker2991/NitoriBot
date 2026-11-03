package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
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

    @Override
    public void playerChatReceived (Component message) {
        handleMessages(message);
    }

    @Override
    public void disguisedChatReceived (Component message) {
        handleMessages(message);
    }

    @Override
    public void systemChatReceived (Component message) {
        handleMessages(message);
    }

    public void handleMessages (Component _message) {
        Component host = Component.text(
            String.format(
                "[%s:%s] ",
                bot.options.host,
                bot.options.port
            )
        ).color(NamedTextColor.BLUE);
        Component component = Component.empty().append(host).append(_message);
        String message = ComponentUtil.componentToAnsi(component);

        bot.logger.log(message);
        //LOGGER.info(message);
    }
}