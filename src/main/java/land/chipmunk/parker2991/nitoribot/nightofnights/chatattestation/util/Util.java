package land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util;

import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.config.ConfigurationManager;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public final class Util {
  private static final Pattern EXTRACTOR = Pattern.compile("^<?([^:<>]*)[>:] (.*)$");
  private static final Pattern CHIPMUNK_EXTRACTOR = Pattern.compile("^\\[.*] ([^›]*) › (.*)$");

  private Util() {

  }

  public static @Nullable String extractDisguisedContent(String fullMsg) {
    final var matcher = EXTRACTOR.matcher(fullMsg);
    return matcher.matches() ? matcher.group(2) : null;
  }

  public static @Nullable SystemExtraction extractSystemContent(String msg) {
    final var chipmunkMatcher = CHIPMUNK_EXTRACTOR.matcher(msg);
    if (chipmunkMatcher.matches()) {
      return new SystemExtraction(chipmunkMatcher.group(1), chipmunkMatcher.group(2));
    } else {
      final var regularExtractor = EXTRACTOR.matcher(msg);
      if (!regularExtractor.matches()) return null;
      // TODO: Handle spaces in names.
      final var sender = regularExtractor.group(1).split(" ");
      if (sender.length == 1 && ConfigurationManager.INSTANCE.config.ignoreCspyLike) return null;
      return new SystemExtraction(sender[sender.length - 1], regularExtractor.group(2));
    }
  }

  public record SystemExtraction(String sender, String content) {

  }
}
