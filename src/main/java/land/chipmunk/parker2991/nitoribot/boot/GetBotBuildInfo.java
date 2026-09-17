package land.chipmunk.parker2991.nitoribot.boot;

import com.google.gson.Gson;
import land.chipmunk.parker2991.nitoribot.Main;
import land.chipmunk.parker2991.nitoribot.data.buildstring.BotBuildInfo;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public class GetBotBuildInfo {
  private static final Gson GSON = new Gson();

  public BotBuildInfo getBuildInfo () {
    Reader reader = new InputStreamReader(
      Objects.requireNonNull(Main.class
        .getResourceAsStream("/info.json"))
    );

    return GSON.fromJson(reader, BotBuildInfo.class);
  }
}
