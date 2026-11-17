package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.PingServerData;
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
    //public final SessionService sessionService = new SessionService();
    //public final MinecraftProtocol protocol = new MinecraftProtocol();
    private final NitoriBot bot;
    private final List<SkinPart> skinParts = new ArrayList<>();
    //public ClientSession client;

    public MCServerModule (NitoriBot bot) {
        this.bot = bot;
    }

    public List<PingServerData> pingServer (String[] host) {
        List<PingServerData> list = new ArrayList<>();
        ClientSession client;
        final SessionService sessionService = new SessionService();
        final MinecraftProtocol protocol = new MinecraftProtocol();

        client = ClientNetworkSessionFactory
            .factory()
            .setAddress(
                host[0],
                Integer.parseInt(host[1])
            )
            .setProtocol(protocol)
            .create();

        client.send(new ServerboundClientInformationPacket(
            "en_us",
            1,
            ChatVisibility.FULL,
            true,
            skinParts,
            HandPreference.RIGHT_HAND,
            false,
            true,
            ParticleStatus.ALL
        ));

        client.setFlag(
            MinecraftConstants.SESSION_SERVICE_KEY,
            sessionService
        );

        client.setFlag(
            MinecraftConstants.SERVER_INFO_HANDLER_KEY,
            (Session session, ServerStatusInfo info) -> {

                list.add(new PingServerData(
                    info
                        .getVersionInfo()
                        .getVersionName(),
                    info
                        .getVersionInfo()
                        .getProtocolVersion(),
                    info
                        .getPlayerInfo()
                        .getOnlinePlayers(),
                    info
                        .getPlayerInfo()
                        .getMaxPlayers(),
                    info.getDescription()
                ));
            }
        );

        client.setFlag(
            MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY,
            (session, pingTime) -> {
            }
        );

        client.connect();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
        }

        return list;
    }
}
