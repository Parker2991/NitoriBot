package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;

import java.util.ArrayList;
import java.util.List;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundDisguisedChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundLoginPacket;

import net.kyori.adventure.text.Component;

import java.time.Instant;
import java.util.BitSet;
import java.util.Collections;

public class ChatModule extends SessionAdapter {
  private Bot bot;
  
  private final List<Listener> listeners = new ArrayList<>();

  @Override
  public void packetReceived (Session session, Packet packet) {
    if (packet instanceof ClientboundSystemChatPacket) systemChat((ClientboundSystemChatPacket) packet);
    else if (packet instanceof ClientboundPlayerChatPacket) playerChat((ClientboundPlayerChatPacket) packet);
    else if (packet instanceof ClientboundDisguisedChatPacket) diguisedChat((ClientboundDisguisedChatPacket) packet);
  }


  public void systemChat (ClientboundSystemChatPacket packet) {
    final Component message = packet.getContent();

    for (Listener listener : listeners) {
      listener.systemChatReceived(message);
    } 
  }

  public void diguisedChat (ClientboundDisguisedChatPacket packet) {
    Component message = packet.getMessage();

    for (Listener listener : listeners) {
      listener.disguisedChatReceived(message);
    }
  }

  public void playerChat (ClientboundPlayerChatPacket packet) {
    final Component unsignedContent = packet.getUnsignedContent();

    for (Listener listener : listeners) {
      listener.playerChatReceived(unsignedContent);
    }
  }// session.sendPacket(new ServerboundChatPacket(message, Instant.now().toEpochMilli(), 0, null, 0, new BitSet(), 0));
  

  public void message (String message) {
    bot.session.send(
      new ServerboundChatPacket(
        message,
        Instant.now().toEpochMilli(),
        0,
        null,
        0,
        new BitSet(),
        0
      )
    );
  }

  public void command (String command) {
    bot.session.send(
      new ServerboundChatCommandPacket(
        command
      )
    );
  }
/*
                command,
                Instant.now().toEpochMilli(),
                0L,
                Collections.emptyList(),
                0,
                new BitSet()
*/
  public ChatModule (Bot bot) {
    this.bot = bot;
    bot.session.addListener(this);
  };

  public static class Listener {
    public boolean playerChatReceived (Component message) { return true; };

    public boolean systemChatReceived (Component message) { return true; };

    public boolean disguisedChatReceived (Component message) { return true; };
  }
  

  //@Override
  //public void packetReceived (Session session, Packet packet) {

  //}

  public void addListener (Listener listener) {
    listeners.add(listener);
  }
}