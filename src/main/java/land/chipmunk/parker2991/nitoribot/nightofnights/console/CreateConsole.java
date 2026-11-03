package land.chipmunk.parker2991.nitoribot.nightofnights.console;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.Main;
import org.jline.reader.*;

import java.util.List;

public class CreateConsole implements Completer {
    private static final List<NitoriBot> servers = Main.Bots;
    public LineReader reader;
    public final String server = "all";


    @Override
    public void complete (
        LineReader reader, ParsedLine line, List<Candidate> candidates
    ) {

    }

    private void handleLine (String line) {
        for (NitoriBot bot : servers) {
            if (line.equals(bot.config.console.prefix + "kill")) System.exit(0);
            bot.chat.send(line);
        }
    }

    public CreateConsole () {
        this.reader = LineReaderBuilder.builder().completer(this).build();
        //this.terminal = TerminalBuilder.builder().build();

        //this.jLineAppender = new JlineAppender();

        JlineAppender.setLineReader(reader);

        Main.executorService.submit(
            () -> {
                while (true) {
                    try {
                        String line;

                        line = reader.readLine(String.format(
                            "[%s] > ",
                            this.server
                        ));

                        handleLine(line);
                    } catch (Exception _) {
                    }
                }
            },
            "Console Thread"
        );

    }
}