package land.chipmunk.parker2991.nitoribot;

import java.util.ArrayList;
import java.util.List;

public class Config {
  //public List Bots = new ArrayList<>();
  
  public Options[] bots = new Options[]{};

  public static class Options {
    public String host;
    public int port;
    public String username;
  };
}
//Configuration.BotOption[] botsOptions = config.bots;