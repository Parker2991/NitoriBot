package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.data.PositionData;
import net.kyori.adventure.text.Component;

import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerPositionPacket;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level.ServerboundAcceptTeleportationPacket;

import java.util.ArrayList;
import java.util.List;

import org.cloudburstmc.math.vector.Vector3d;

public class PositionModule extends SessionAdapter {
  private Bot bot;

  private final List<Listener> listeners = new ArrayList<>();

  public Vector3d positionAsVector = null;

  public PositionData positionAsInteger;

  @Override
  public void packetReceived (Session session, Packet packet) {
    if (packet instanceof ClientboundPlayerPositionPacket) packetReceived((ClientboundPlayerPositionPacket) packet);
  }

  public void packetReceived (ClientboundPlayerPositionPacket packet) {
    Vector3d getPosition = packet.getPosition();

    int x = (int) Math.round(getPosition.getX());
    int y = (int) Math.round(getPosition.getY());
    int z = (int) Math.round(getPosition.getZ());
    
    positionAsInteger = new PositionData(x, y, z);
    positionAsVector = getPosition;

    bot.session.send(
      new ServerboundAcceptTeleportationPacket(
        packet.getId()
      )
    );

    for (Listener listener : listeners) {
      listener.botMoved();
    }
  } 

  public static class Listener {
    public boolean botMoved () { return true; };
  }
  
  public void addListener (Listener listener) {
    listeners.add(listener);
  }
  
  public PositionModule (Bot bot) {
    this.bot = bot;
    bot.session.addListener(this);
  }
}