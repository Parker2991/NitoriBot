package land.chipmunk.parker2991.nitoribot;

import java.util.List;

public class Config {
  public List<String> prefixes;

  public static class Core {
    public Area area = new Area();
    public String coreName;
  }

  public static class Area {
    public Position start = new Position();
    public Position end = new Position();
  }

  public static class Position {
    public int x = 0;
    public int y = 0;
    public int z = 0;
  }

  public static class CommandColors {
    public String primary = "blue";
    public String secondary = "aqua";
    public String tertiary = "dark_aqua";
  }

  public static class HelpCommandColors {
    public String Public = "blue";
    public String trusted = "dark_aqua";
    public String admin = "aqua";
    public String owner = "light_purple";
    public String console = "dark_purple";
  }

  public static class Console {
    public String prefix = "c.";
  }

  public static class Colors {
    public String integer = "gold";
    public CommandColors commands = new CommandColors();
    public HelpCommandColors help = new HelpCommandColors();
  }

  public Colors colors = new Colors();

  public Core core = new Core();

  public Console console = new Console();

  public Options[] bots = new Options[]{};

  public static class Options {
    public String host;
    public int port;
    public boolean useProxy;
    public String serverName;
    public String username;
    public int reconnectDelay;
    public int selfcareInterval;
    public String mode;
  };
}
