package land.chipmunk.parker2991.nitoribot.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

// credits to Werkku and OptimisticDev for this
public class ErrorToString {
  public static String ErrorToString (Throwable error) {
    final var byteArrayOut = new ByteArrayOutputStream();
    final var outWriter = new OutputStreamWriter(byteArrayOut);
    final var printWriter = new PrintWriter(outWriter);
    error.printStackTrace(printWriter);
    printWriter.flush();
    return byteArrayOut.toString(StandardCharsets.UTF_8);
  }
}
