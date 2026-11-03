package land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util;

import org.jspecify.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.stream.IntStream;

public final class ChatEncoding {
  public static final int[] ENCODING;
  public static final int[] DECODING;

  static {
    ENCODING = IntStream.concat(
        IntStream.range(0x21, 0x7E),
        IntStream.range(0xA1, 0x010000)
      )
      .filter(c -> c != '&' && c != '§')
      .limit(256)
      .toArray();

    final int max = Arrays.stream(ENCODING).max().orElseThrow();
    DECODING = new int[max + 1];
    Arrays.fill(DECODING, -1);

    for (int i = 0; i < ENCODING.length; i++) {
      DECODING[ENCODING[i]] = i;
    }
  }

  public static String encode(byte[] input) {
    final var builder = new StringBuilder(input.length);

    for (final byte b : input) {
      builder.appendCodePoint(ENCODING[(int) b & 0xFF]);
    }

    return builder.toString();
  }

  public static byte @Nullable [] decode(String input) {
    final var out = new ByteArrayOutputStream();

    for (int i = 0; i < input.length(); i++) {
      final var ch = input.codePointAt(i);
      if (ch < 0 || ch >= DECODING.length) return null;
      final int b = DECODING[ch];
      if (b == -1) return null;
      out.write(b);
    }

    return out.toByteArray();
  }
}
