package land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Pattern;

public final class Constants {
  public static final Logger LOGGER = LoggerFactory.getLogger("chatattestation");
  public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  public static final Pattern SIGNED_MESSAGE_PATTERN = Pattern.compile("\\$\\$(.*)\\$\\$(.*)$");
  public static final ThreadFactory DAEMON_THREAD_FACTORY = r -> {
    final var t = new Thread(r);
    t.setDaemon(true);
    return t;
  };
  public static final int MESSAGE_LIMIT = 256;
  public static final ExecutorService SIGN_EXECUTOR = Executors.newSingleThreadExecutor();
  public static final Path storageDir = Path.of("chatattestation");

  private Constants() {

  }
}
