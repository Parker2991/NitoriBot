package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.selfcare.entity.GamemodeSelfcare;
import land.chipmunk.parker2991.nitoribot.selfcare.entity.PermissionSelfcare;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundDisconnectPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundChangeGameModePacket;
import org.geysermc.mcprotocollib.protocol.packet.login.clientbound.ClientboundLoginFinishedPacket;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SelfcareModule implements Listener {
    private final Bot bot;
    public ScheduledFuture<?> timer;
    public PermissionSelfcare permission;

    public GamemodeSelfcare gamemode;

    public int entityId;

    public SelfcareModule (Bot bot) {
        this.bot = bot;
        bot.listenerManager.addListener(this);
        loadSelfcare();
    }

    public void loadSelfcare () {
        this.permission = new PermissionSelfcare(bot);
        this.gamemode = new GamemodeSelfcare(bot);
    }

    @Override
    public void packetReceived (Packet packet) {
        switch (packet) {
            case ClientboundLoginFinishedPacket p -> login(p);
            case ClientboundDisconnectPacket p -> disconnect(p);
            default -> {
            }
        }
    }

    public void login (ClientboundLoginFinishedPacket packet) {
        if (bot.options.mode.equals("totalfreedom")) return;
        timer = bot.executor.scheduleAtFixedRate(() -> {
            if (permission.level < 2 && bot.loggedIn) bot.chat.command("minecraft:op @s[type=player]");
            else if (gamemode.gamemode != 1) bot.session.send(new ServerboundChangeGameModePacket(GameMode.CREATIVE));
        }, 0, bot.options.selfcareInterval, TimeUnit.MILLISECONDS);
    }

    public void disconnect (ClientboundDisconnectPacket event) {
        timer.cancel(true);
    }
}