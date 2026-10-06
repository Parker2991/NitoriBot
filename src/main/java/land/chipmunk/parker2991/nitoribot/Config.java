package land.chipmunk.parker2991.nitoribot;

import java.util.List;

public class Config {
    public List<String> prefixes;

    public static class Core {
        public final Area area = new Area();
        public String coreName;
    }

    public static class Area {
        public final Position start = new Position();
        public final Position end = new Position();
    }

    public static class Position {
        public int x;
        public int y;
        public int z;
    }

    public static class CommandColors {
        public String primary;
        public String secondary;
        public String tertiary;
    }

    public static class HelpCommandColors {
        public String Public;
        public String trusted;
        public String admin;
        public String owner;
        public String console;
    }

    public static class Console {
        public String prefix;
    }

    public static class Colors {
        public String integer;
        public final CommandColors commands = new CommandColors();
        public final HelpCommandColors help = new HelpCommandColors();
    }

    public final Colors colors = new Colors();

    public final Core core = new Core();

    public final Console console = new Console();

    public final Options[] bots = new Options[]{ };

    public static class Options {
        public String host;
        public int port;
        public boolean useProxy;
        public String serverName;
        public String username;
        public int reconnectDelay;
        public int selfcareInterval;
        public String mode;
    }

}
