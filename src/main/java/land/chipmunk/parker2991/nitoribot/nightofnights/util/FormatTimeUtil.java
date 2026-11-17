package land.chipmunk.parker2991.nitoribot.nightofnights.util;

public class FormatTimeUtil {
    public static String format (double time) {
        final int hours = (int) Math.floor(time / (60 * 60));
        final int minutes = (int) Math.floor((time % (60 * 60)) / 60);
        final int seconds = (int) Math.floor(time % 60);

        return String.format(
            "%s Hours %s Minutes %s Seconds",
            hours,
            minutes,
            seconds
        );
    }
}