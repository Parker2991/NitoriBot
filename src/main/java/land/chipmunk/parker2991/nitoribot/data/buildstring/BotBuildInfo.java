package land.chipmunk.parker2991.nitoribot.data.buildstring;

import com.google.gson.JsonObject;

import java.util.List;

public class BotBuildInfo {
  public static class BuildInfo {
    public List<JsonObject> botName;
    public String version;
    public List<JsonObject> codename;
    public String build;
    public String versionReleaseDate;
    public String initialBotRelease;
  }

  public BuildInfo buildstring = new BuildInfo();

  public String owner;

  public static class Contributors {
    public String name;
    public String reason;
  }

  public Contributors[] contributors = new Contributors[]{};
}