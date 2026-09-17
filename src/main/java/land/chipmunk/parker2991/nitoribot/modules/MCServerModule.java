package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.data.PingServerData;
import org.geysermc.mcprotocollib.auth.SessionService;
import org.geysermc.mcprotocollib.network.ClientSession;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.factory.ClientNetworkSessionFactory;
import org.geysermc.mcprotocollib.protocol.MinecraftConstants;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.HandPreference;
import org.geysermc.mcprotocollib.protocol.data.game.setting.ChatVisibility;
import org.geysermc.mcprotocollib.protocol.data.game.setting.ParticleStatus;
import org.geysermc.mcprotocollib.protocol.data.game.setting.SkinPart;
import org.geysermc.mcprotocollib.protocol.data.status.ServerStatusInfo;
import org.geysermc.mcprotocollib.protocol.packet.common.serverbound.ServerboundClientInformationPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MCServerModule {
  private Bot bot;

  public ClientSession client;
  
  public final SessionService sessionService = new SessionService();
  
  public final MinecraftProtocol protocol = new MinecraftProtocol();
  
  private final List<SkinPart> skinParts = new ArrayList<>();

  public PingServerData pingServer (String[] host) {
    AtomicReference<PingServerData> serverData = new AtomicReference<>();

    client = ClientNetworkSessionFactory.factory()
      .setAddress("kaboom.pw", 25565)
      //.setAddress(host[0], Integer.parseInt(host[1]))
      .setProtocol(protocol)
      .create();
    
    client.send(
      new ServerboundClientInformationPacket(
        "en_us",
        1,
        ChatVisibility.FULL,
        true,
        skinParts,
        HandPreference.RIGHT_HAND,
        false,
        true,
        ParticleStatus.ALL
      )
    );

    client.setFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);

    client.setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, (Session session, ServerStatusInfo info) -> {
      assert info.getVersionInfo() != null;
      assert info.getPlayerInfo() != null;

      serverData.set(new PingServerData(
        info.getVersionInfo().getVersionName(),
        info.getVersionInfo().getProtocolVersion(),
        info.getPlayerInfo().getOnlinePlayers(),
        info.getPlayerInfo().getMaxPlayers(),
        info.getDescription()
      ));
    });

    System.out.println(serverData);

    client.setFlag(MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY, (session, pingTime) ->
      serverData.get().latency(pingTime)
    );
    bot.executor.submit(() -> {
      client.connect();
    });

    return serverData.get();
  }
  
  public MCServerModule (Bot bot) {
    this.bot = bot;

  }
}
