package land.chipmunk.parker2991.nitoribot.data;

import net.kyori.adventure.text.Component;

public record PingServerData(
    //String ip,
    //int port,
    String versionName, long protocolVersion, int playersOnline, int maxPlayers, Component description
    //long latency
) {
    public void latency (long pingTime) {
        System.out.println(versionName);
    }
}
