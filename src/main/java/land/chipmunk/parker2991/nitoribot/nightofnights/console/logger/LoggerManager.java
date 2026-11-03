package land.chipmunk.parker2991.nitoribot.nightofnights.console.logger;

import land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.slf4j.*;

public class LoggerManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerManager.class);

    public String convertComponentToAnsi (Component component) {
        return ComponentUtil.componentToAnsi(component);
    }
    private String getLoggerType (LoggerType logType) {
        return switch (logType) {
            case LoggerType.COMMAND -> convertComponentToAnsi(Component.text("COMMAND").color(NamedTextColor.YELLOW));
            case LoggerType.LOG -> convertComponentToAnsi(Component.text("LOG").color(NamedTextColor.GOLD));
            case LoggerType.RECONNECT -> convertComponentToAnsi(Component.text("RECONNECT").color(NamedTextColor.AQUA));
        };
    }

    public void log (String message) {
        MDC.putCloseable("customLevel", getLoggerType(LoggerType.LOG));
        LOGGER.info(message);
    }

    public void command (String message) {
        MDC.putCloseable("customLevel", getLoggerType(LoggerType.COMMAND));
        LOGGER.info(message);
    }

    public void reconnect (String message) {
        MDC.putCloseable("customLevel", getLoggerType(LoggerType.RECONNECT));
        LOGGER.info(message);
    }
}
