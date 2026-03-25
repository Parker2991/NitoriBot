package land.chipmunk.parker2991.nitoribot;

public class Config {
  
  public Options[] bots = new Options[]{};
  //public ArrayList Bots = new ArrayList<>();

  public static class Options {
    public String host;
    public int port;
    public String username;
    public int reconnectDelay;
  };
}
