package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.listeners.*;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundDisguisedChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatPacket;

import net.kyori.adventure.text.Component;

import java.time.Instant;
import java.util.BitSet;

public class ChatModule extends Listener {
  private Bot bot;

  @Override
  public void packetReceived (Session session, Packet packet) {
    if (packet instanceof ClientboundSystemChatPacket) systemChat((ClientboundSystemChatPacket) packet);
    else if (packet instanceof ClientboundPlayerChatPacket) playerChat((ClientboundPlayerChatPacket) packet);
    else if (packet instanceof ClientboundDisguisedChatPacket) diguisedChat((ClientboundDisguisedChatPacket) packet);
  }

  public String parseChatTypes (int chatType) {
    String getTranslation = bot.registry.chatTypes.get(chatType);

    return getTranslation;
  }

  public void systemChat (ClientboundSystemChatPacket packet) {
    final Component message = packet.getContent();

    for (Listener listener : bot.ListenerManager.listeners) {
      listener.systemChatReceived(message);
    }
  }

  public void diguisedChat (ClientboundDisguisedChatPacket packet) {
    Component getMessage = packet.getMessage();
    Component targetUsername = packet.getName();
    Component message;

    final String parseChatTypes = parseChatTypes(packet.getChatType().id());

    if (packet.getChatType().id() == 4) {
      message = Component.translatable(
        parseChatTypes,
        getMessage
      );
    } else {
      message = Component.translatable(
        parseChatTypes,
        targetUsername,
        getMessage
      );
    }

    for (Listener listener : bot.ListenerManager.listeners) {
      listener.disguisedChatReceived(message);
    }
  }

  public void playerChat (ClientboundPlayerChatPacket packet) {
    final Component unsignedContent = packet.getUnsignedContent();

    for (Listener listener : bot.ListenerManager.listeners) {
      listener.playerChatReceived(unsignedContent);
    }
  } 

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

  public ChatModule (Bot bot) {
    this.bot = bot;

    bot.ListenerManager.addListener(this);
  };
}