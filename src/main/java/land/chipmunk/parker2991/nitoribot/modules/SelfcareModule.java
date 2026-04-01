package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.selfcare.entity.*;

import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundChangeGameModePacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.protocol.packet.login.clientbound.ClientboundLoginFinishedPacket;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SelfcareModule extends SessionAdapter {
  public ScheduledFuture timer;

  private Bot bot;

  public PermissionSelfcare permission;

  public GamemodeSelfcare gamemode;

  public int entityId;

  public void loadSelfcare () {
    this.permission = new PermissionSelfcare(bot);
    this.gamemode = new GamemodeSelfcare(bot);
  }

  @Override
  public void packetReceived (Session session, Packet packet) {
    if (packet instanceof ClientboundLoginFinishedPacket) login((ClientboundLoginFinishedPacket) packet);
  }

  public void login (ClientboundLoginFinishedPacket packet) {
    timer = bot.executor.scheduleAtFixedRate(() -> {
      if (permission.level < 2 && bot.loggedIn == true) bot.chat.command("minecraft:op @s[type=player]");
      else if (gamemode.gamemode != 1) bot.session.send(
        new ServerboundChangeGameModePacket(
          GameMode.CREATIVE
        )
      );
    }, 0, bot.options.selfcareInterval, TimeUnit.MILLISECONDS);
  }

  public void disconnect (DisconnectedEvent event) {
    timer.cancel(true);
  }

  public SelfcareModule (Bot bot) {
    this.bot = bot;
    bot.session.addListener(this);
    loadSelfcare();
  }
}