package land.chipmunk.parker2991.nitoribot.nightofnights.console;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.jline.reader.LineReader;
import org.jspecify.annotations.Nullable;

import java.nio.charset.StandardCharsets;

public class JlineAppender extends AppenderBase<ILoggingEvent> {
    private static LineReader reader;
    private PatternLayoutEncoder encoder;
    //private PatternLayoutEncoder encoder = new PatternLayoutEncoder();

    public static void setLineReader (@Nullable LineReader lineReader) {
        reader = lineReader;
    }

    @Override
    public void start () {
        super.start();
        encoder.start();
    }

    @Override
    public void stop () {
        encoder.stop();
        super.stop();
    }


    public void setEncoder (PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void append (ILoggingEvent event) {
        PatternLayoutEncoder enc = encoder;
        String message = new String(enc.encode(event), StandardCharsets.UTF_8).stripTrailing();

        if (reader != null) {
            reader.printAbove(message);
        } else {
            System.out.println(message);
        }
    }
}