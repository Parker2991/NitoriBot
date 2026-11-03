package land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util;

import com.google.common.primitives.Ints;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.Objects;

public final class NormalizationUtil {
  private static final int[] CONFUSABLES;

  static {
    int[] confusables = new int[0];
    final String read;
    final var resourceStream = Objects.requireNonNull(NormalizationUtil.class.getClassLoader().getResourceAsStream("confusables.txt"));

    try {
      read = new String(resourceStream.readAllBytes(), StandardCharsets.UTF_8);
      resourceStream.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to read confusables list", e);
    }

    final var hexFormat = HexFormat.of();
    final var lines = read.lines().iterator();
    while (lines.hasNext()) {
      var line = lines.next();
      if (line.startsWith("#")) continue;
      final var split = line.split(";");
      if (split.length < 2) continue;
      final int confusable = intFromByteArrayWithPadding(hexFormat.parseHex(padHex(split[0].trim())));
      if (confusable <= 127) continue;
      final var replacement = split[1].trim();
      if (replacement.split(" ").length != 1) continue;
      final int replacementCodepoint = intFromByteArrayWithPadding(hexFormat.parseHex(padHex(replacement)));
      if (replacementCodepoint > 127) continue;
      if (confusables.length <= confusable) {
        int[] newConfusables = new int[confusable + 1];
        Arrays.fill(newConfusables, Integer.MIN_VALUE);
        System.arraycopy(confusables, 0, newConfusables, 0, confusables.length);
        confusables = newConfusables;
      }

      confusables[confusable] = replacementCodepoint;
    }

    CONFUSABLES = confusables;
  }

  private static String padHex(String input) {
    if (input.length() % 2 != 0) return "0" + input;
    return input;
  }


  private static int intFromByteArrayWithPadding(byte[] buf) {
    if (buf.length != 4) {
      final byte[] intermediate = new byte[4];
      System.arraycopy(buf, 0, intermediate, 4 - buf.length, buf.length);
      buf = intermediate;
    }

    return Ints.fromByteArray(buf);
  }

  public static String normalizeString(String input) {
    input = Normalizer.normalize(input, Normalizer.Form.NFKC);
    final var output = new StringBuilder();

    for (int i = 0; i < input.length(); i++) {
      final int codepoint = input.codePointAt(i);
      final int replacement;
      if (CONFUSABLES.length <= codepoint
        || (replacement = CONFUSABLES[codepoint]) == Integer.MIN_VALUE)
        output.appendCodePoint(codepoint);
      else {
        output.appendCodePoint(replacement);
      }
    }

    return output.toString();
  }
}
