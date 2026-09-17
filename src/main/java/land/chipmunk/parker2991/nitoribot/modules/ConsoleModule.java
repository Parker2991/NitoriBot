package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.Config;
import land.chipmunk.parker2991.nitoribot.Main;
import org.jline.reader.*;

import java.util.List;

public class ConsoleModule implements Completer {
  private static final List<Bot> servers = Main.Bots;

  public LineReader reader;

  public String server = "all";

  @Override
  public void complete (LineReader reader, ParsedLine line, List<Candidate> candidates) {

  }

  private void handleLine (String line) {
    for (Bot bot : servers) {
      if (line.equals("c.kill")) System.exit(0);
      bot.chat.send(line);
    }
  }

  public ConsoleModule (Config config) {
    this.reader = LineReaderBuilder.builder()
      .completer(this)
      .build();

    Main.executorService.submit(() -> {
      while (true) {
        try {
          String line = null;

          line = reader.readLine(String.format("[%s] > ", this.server));

          handleLine(line);
        } catch (Exception e) {}
      }
    }, "Console Thread");
  }
}
