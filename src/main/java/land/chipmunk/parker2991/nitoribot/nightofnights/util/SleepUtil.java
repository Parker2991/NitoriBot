package land.chipmunk.parker2991.nitoribot.nightofnights.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class SleepUtil {
    public static CompletableFuture sleep (long ms) {
        final Executor executor = CompletableFuture.delayedExecutor(
            ms,
            TimeUnit.MILLISECONDS
        );

        return CompletableFuture.runAsync(() -> {}, executor);
    }
}

/*
async function delay(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}
 Executor delayed = CompletableFuture.delayedExecutor(ms, TimeUnit.MILLISECONDS);
        return CompletableFuture.runAsync(() -> {}, delayed);
 */