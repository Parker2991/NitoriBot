package land.chipmunk.parker2991.nitoribot;

public class Config {
  public static class Core {
    public Area area = new Area();
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

  public Core core = new Core();

  public Options[] bots = new Options[]{};

  public static class Options {
    public String host;
    public int port;
    public String username;
    public int reconnectDelay;
    public int selfcareInterval;
  };
}
