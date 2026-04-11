package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.listeners.*;
import land.chipmunk.parker2991.nitoribot.chatparsers.*;
import land.chipmunk.parker2991.nitoribot.data.chat.*;
import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundDisguisedChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatPacket;

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.time.Instant;
import java.util.BitSet;

public class ChatModule extends Listener {
  private Bot bot;

  public final List<ParseChatData> chatParsers = new ArrayList<>();

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
      message = getMessage;
    } else {
      message = Component.translatable(
        parseChatTypes,
        targetUsername,
        getMessage
      );
    }
/*
      parseMessage(message, {
        players: bot.players,
        getMessageAsPrismarine: bot.getMessageAsPrismarine,
        chatType: "minecraft:system_chat",
      }
          public PlayerProfileData sender;
  public Component contents;
  public String chatType;
  public Component senderName;
*/
   // parseMessage(
     // message, 
      //new PlayerMessageData(

      //)
    //);

    for (Listener listener : bot.ListenerManager.listeners) {
      listener.disguisedChatReceived(message);
    }
  }

  public void playerChat (ClientboundPlayerChatPacket packet) {
    final Component unsignedContent = packet.getUnsignedContent();
    final Component content = Component.text(packet.getContent());
    //final PlayerProfileData sender = bot.players.getPlayerUUID(packet.getSender());
    //System.out.println(packet.getSender());

    for (Listener listener : bot.ListenerManager.listeners) {
      listener.playerChatReceived(unsignedContent);
    }

    parseMessage(
      unsignedContent,
      new PlayerMessageData(
        bot.players.getPlayerUUID(packet.getSender()),
        content,
        "minecraft:chat",
        bot.players.getPlayerUUID(packet.getSender()).displayName
      )
    );
//final PlayerMessage playerMessage = new PlayerMessage(sender, parsedFromMessage.contents(), "minecraft:chat", packet.getName());
    //parseMessage(unsignedContent, packet);
  }

  public void parseMessage (Component message, PlayerMessageData data) {
    try {
      PlayerMessageData parsed = null;

      for (ParseChatData parser : chatParsers) {
        parsed = parser.parse(message);
        if (parsed != null) break;
      };

      if (parsed == null) return;

      for (Listener listener : bot.ListenerManager.listeners) {
        listener.parsedMessage(message, data);
      }

    } catch (Exception ignore) {}
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

  public void tellraw (String selector, Component message) {
    bot.core.run("minecraft:tellraw " + selector + " " + GsonComponentSerializer.gson().serialize(message));
  }

  public ChatModule (Bot bot) {
    this.bot = bot;
  
    chatParsers.add(new KaboomChatParser(bot));

    bot.ListenerManager.addListener(this);
  };
}