package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundSetEntityDataPacket;

public class TextDisplayModule extends Listener {
  private Bot bot;

  private static final int TEXT_DISPLAY_ENTITY_ID = 32;

  private int entityId;

  @Override
  public void packetReceived (Session session, Packet packet) {
    if (packet instanceof ClientboundSetEntityDataPacket) getTextDisplay((ClientboundSetEntityDataPacket) packet);
  }

  private void getTextDisplay (ClientboundSetEntityDataPacket packet) {
    for (EntityMetadata<?, ?> metaData : packet.getMetadata()) {
      if (metaData.getId() == TEXT_DISPLAY_ENTITY_ID) {
        Component value = (Component) metaData.getValue();
        System.out.println(ComponentUtil.componentToAnsi(value));
      }
    }
  }

  public TextDisplayModule (Bot bot) {
    this.bot = bot;
    bot.listenerManager.addListener(this);
  }
}