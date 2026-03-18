package land.chipmunk.parker2991.nitoribot.logger;

import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LoggerManager {
  private static final Logger logger = LoggerFactory.getLogger(LoggerManager.class);

  private static final DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("hh:mm:ss");

  private static final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM/dd/yyyy");

  public static String prefix (Component type, Component host, String message) {
    Component component;
    
    ZoneId timezone = ZoneId.of("America/Chicago");

    ZonedDateTime ZoneDate = ZonedDateTime.now(timezone);

    LocalDate date = ZoneDate.toLocalDate();
    LocalTime time = ZoneDate.toLocalTime();

    component = Component.translatable(
      "[%s %s %s] [%s] %s",
      Component.translatable(formatTime.format(time)).color(NamedTextColor.DARK_BLUE),
      Component.translatable(formatDate.format(date)).color(NamedTextColor.AQUA),
      type,
      host,
      Component.translatable(message).color(NamedTextColor.WHITE)
    ).color(NamedTextColor.DARK_GRAY);

    return ComponentUtil.componentToAnsi(component);
  };

  public static void LOG (Component host, String message) {
    Component type = Component.text("log").color(NamedTextColor.GOLD);

    System.out.println(prefix(type, host, message));
  };

  public static void INFO (Component host, String message) {
    Component type = Component.text("info").color(NamedTextColor.GREEN);

    System.out.println(prefix(type, host, message));
  };

  public static void COMMAND (Component host, String message) {
    Component type = Component.text("command").color(NamedTextColor.YELLOW);

    System.out.println(prefix(type, host, message));
  };

  public static void WARN (Component host, String message) {
    Component type = Component.text("warn").color(NamedTextColor.YELLOW);
    System.out.println(prefix(type, host, message));
  };

  public static void ERROR (Component host, String message) {
    Component type = Component.text("error").color(NamedTextColor.DARK_RED);

    System.out.println(prefix(type, host, message));
  };
}
