package land.chipmunk.parker2991.nitoribot.nightofnights.boot;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.buildstring.RepoCommitInfo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class GetRepoInfo {
    private static final Gson GSON = new Gson();

    public RepoCommitInfo getRepoInfo () {
        final String repo = "https://code.chipmunk.land/api/v1/repos/Parker2991/NitoriBot/commits?sha=main";
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).connectTimeout(Duration.ofSeconds(
            20)).build();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(repo)).timeout(Duration.ofMillis(20)).header("accept",
                                                                                                                    "application/json"
        ).build();

        RepoCommitInfo info = null;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonElement json = JsonParser.parseString(response.body());

            info = GSON.fromJson(json.getAsJsonArray().get(0), RepoCommitInfo.class);

        } catch (Exception e) {
            ZoneId timezone = ZoneId.of("America/Chicago");
            String zoneDate = String.valueOf(ZonedDateTime.now(timezone));
            //e.printStackTrace(System.err);
            info = new RepoCommitInfo();
            info.created = zoneDate;
            info.commits = 0;
            info.sha = "N/A";
        }
        return info;
    }

}
