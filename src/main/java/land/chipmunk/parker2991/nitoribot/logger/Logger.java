package land.chipmunk.parker2991.nitoribot.logger;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.Main;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("hh:mm:ss");

    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public Logger () {

    }

    public static void LOG (Bot bot, String message) {
        new Logger().prefix(LoggerType.LOG, bot, message);
    }

    public static void DEBUG (Bot bot, String message) {
        new Logger().prefix(LoggerType.DEBUG, bot, message);
    }

    public static void ERROR (Bot bot, String message) {
        new Logger().prefix(LoggerType.ERROR, bot, message);
    }

    public static void WARN (Bot bot, String message) {
        new Logger().prefix(LoggerType.WARN, bot, message);
    }

    public static void COMMAND (Bot bot, String message) {
        new Logger().prefix(LoggerType.COMMAND, bot, message);
    }

    public static void RECONNECT (Bot bot, String message) {
        new Logger().prefix(LoggerType.RECONNECT, bot, message);
    }

    public static void INFO (Bot bot, String message) {
        new Logger().prefix(LoggerType.INFO, bot, message);
    }

    public Component getLoggerType (LoggerType type) {
        Component logType = null;
        switch (type) {
            case LoggerType.LOG -> logType = Component.text("LOG").color(NamedTextColor.GOLD);
            case LoggerType.COMMAND -> logType = Component.text("COMMAND").color(NamedTextColor.GREEN);
            case LoggerType.ERROR -> logType = Component.text("ERROR").color(NamedTextColor.DARK_RED);
            case LoggerType.WARN -> logType = Component.text("WARN").color(NamedTextColor.YELLOW);
            case LoggerType.INFO -> logType = Component.text("INFO").color(NamedTextColor.DARK_GREEN);
            case LoggerType.RECONNECT -> logType = Component.text("RECONNECT").color(NamedTextColor.AQUA);
            case LoggerType.DEBUG -> logType = Component.text("DEBUG").color(NamedTextColor.GOLD);
        }
        return logType;
    }

    private void prefix (LoggerType loggerType, @Nullable Bot bot, String message) {
        Component component;
        Component host;

        ZoneId timezone = ZoneId.of("America/Chicago");
        ZonedDateTime zoneDate = ZonedDateTime.now(timezone);
        LocalDate date = zoneDate.toLocalDate();
        LocalTime time = zoneDate.toLocalTime();

        Component type = getLoggerType(loggerType);

        if (bot == null) host = Component.text("NitoriBot Jar").color(NamedTextColor.BLUE);
        else host = Component.text(bot.options.host + ":" + bot.options.port).color(NamedTextColor.BLUE);

        component = Component.translatable(
            "[%s %s %s] [%s] [%s] %s",
            Component.translatable(formatTime.format(time)).color(NamedTextColor.BLUE),
            Component.translatable(formatDate.format(date)).color(NamedTextColor.BLUE),
            type,
            Component.translatable(Thread.currentThread().getName()).color(NamedTextColor.BLUE),
            host,
            Component.translatable(message).color(NamedTextColor.WHITE)
        ).color(NamedTextColor.DARK_BLUE);

        String log = ComponentUtil.componentToAnsi(component);

        if (bot == null) System.out.println(log);
        else Main.console.reader.printAbove(log);
    }
}
