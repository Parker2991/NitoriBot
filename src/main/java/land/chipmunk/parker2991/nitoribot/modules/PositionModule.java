package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.data.PositionData;
import land.chipmunk.parker2991.nitoribot.listeners.Listener;
import org.cloudburstmc.math.vector.Vector3d;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerPositionPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level.ServerboundAcceptTeleportationPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosPacket;

import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PositionModule extends Listener {
  private Bot bot;

  public ScheduledFuture<?> timer;

  private int i = 0;

  public Vector3d positionAsVector = null;

  public PositionData positionAsInteger;

  private int x;
  private int z;

  @Override
  public void packetReceived (Session session, Packet packet) {
    if (packet instanceof ClientboundPlayerPositionPacket) packetReceived((ClientboundPlayerPositionPacket) packet);
  }

  public void packetReceived (ClientboundPlayerPositionPacket packet) {
    Vector3d getPosition = packet.getPosition();

    x = (int) Math.round(getPosition.getX());
    int y = (int) Math.round(getPosition.getY());
    z = (int) Math.round(getPosition.getZ());

    positionAsInteger = new PositionData(x, y, z);
    positionAsVector = getPosition;

    if (bot.options.mode.equals("totalfreedom")) {
      if (i < 5) timer = bot.executor.scheduleAtFixedRate(() -> {
        System.out.println(i);
        x += 1;
        z += 1;

        bot.session.send(
          new ServerboundMovePlayerPosPacket(
            false,
            false,
            x,
            y,
            z
          )
        );
        System.out.println("moved");
        System.out.println(x);
        i++;
        if (i > 5) timer.cancel(true);
      }, 5000, 5000, TimeUnit.MILLISECONDS);
    }

    bot.session.send(
      new ServerboundAcceptTeleportationPacket(
        packet.getId()
      )
    );

    for (Listener listener : bot.listenerManager.listeners) {
      if (! Objects.equals(bot.options.mode, "kaboom")) return;
      listener.botMoved();
    }
  }

  public PositionModule (Bot bot) {
    this.bot = bot;

    bot.listenerManager.addListener(this);
  }
}