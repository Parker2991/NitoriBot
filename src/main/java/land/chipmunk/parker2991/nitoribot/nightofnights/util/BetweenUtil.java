package land.chipmunk.parker2991.nitoribot.nightofnights.util;

public class BetweenUtil {
    public static double between (
        int min,
        int max
    ) {
        return Math.floor(Math.random() * (max - min) + min);
    }
}