package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.Bot;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatparsers.KaboomChatParser;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.ChatTranslationsData;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.ChatType;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.chat.ParseChatData;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.chat.PlayerMessageData;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundDisguisedChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatPacket;

import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;

public class ChatModule implements Listener {
    public final List<ParseChatData> chatParsers;
    private final Bot bot;

    public ChatModule (Bot bot) {
        this.bot = bot;

        this.chatParsers = List.of(new KaboomChatParser(bot));

        bot.listenerManager.addListener(this);
    }

    @Override
    public void packetReceived (Packet packet) {
        switch (packet) {
            case ClientboundSystemChatPacket p -> systemChat(p);
            case ClientboundPlayerChatPacket p -> playerChat(p);
            case ClientboundDisguisedChatPacket p -> disguisedChat(p);
            default -> {
            }
        }
    }

    public String parseChatTypes (int chatType) {
        List<String> translations = new ArrayList<>();

        for (ChatTranslationsData translation : bot.registry.chatTypes) {
            translations.add(translation.chat.translation_key);
        }

        return translations.get(chatType);
    }

    public void systemChat (ClientboundSystemChatPacket packet) {
        final Component message = packet.getContent();

        for (Listener listener : bot.listenerManager.listeners) {
            listener.systemChatReceived(message);
        }
    }

    public void disguisedChat (ClientboundDisguisedChatPacket packet) {
        Component getMessage = packet.getMessage();
        Component targetUsername = packet.getName();

        Component message;

        final String parseChatTypes = parseChatTypes(packet.getChatType().id());

        if (packet.getChatType().id() == 4) {
            message = getMessage;
        } else {
            message = Component.translatable(parseChatTypes, targetUsername, getMessage);
        }

        for (Listener listener : bot.listenerManager.listeners) {
            listener.disguisedChatReceived(message);
        }


        parseMessage(
            ChatType.DISGUISED,
            message,
            new PlayerMessageData(
                bot.players.getPlayerByUsername(ComponentUtil.componentToString(packet.getName())),
                packet.getMessage(),
                ChatType.DISGUISED,
                packet.getName()
            )
        );

    }

    public void playerChat (ClientboundPlayerChatPacket packet) {
        final Component unsignedContent = packet.getUnsignedContent();
        final Component content = Component.text(packet.getContent());

        for (Listener listener : bot.listenerManager.listeners) {
            listener.playerChatReceived(unsignedContent);
        }

        parseMessage(
            ChatType.PLAYER,
            unsignedContent,
            new PlayerMessageData(
                bot.players.getPlayerUUID(packet.getSender()),
                content, ChatType.PLAYER,
                Objects.requireNonNull(bot.players.getPlayerUUID(packet.getSender())).displayName
            )
        );
    }

    public void parseMessage (ChatType chatType, Component message, PlayerMessageData data) {
        try {
            PlayerMessageData parsed = null;

            for (ParseChatData parser : chatParsers) {
                parsed = parser.parse(chatType, message);
                if (parsed != null) break;
            }


            if (parsed == null) return;

            for (Listener listener : bot.listenerManager.listeners) {
                if (data.chatType() == ChatType.DISGUISED) {
                    final PlayerMessageData playerMessageData = new PlayerMessageData(parsed.sender(), parsed.contents(), ChatType.DISGUISED, data.senderName());
                    listener.parsedMessage(playerMessageData);
                }

                else listener.parsedMessage(data);
            }

        } catch (Exception ignore) {
        }
    }

    public void message (String message) {
        bot.session.send(new ServerboundChatPacket(message, Instant.now().toEpochMilli(), 0, null, 0, new BitSet(), 0));
    }

    public void command (String command) {
        bot.session.send(new ServerboundChatCommandPacket(command));
    }

    public void send (String message) {
        if (message.startsWith("/")) bot.chat.command(message.substring(1));

        else bot.chat.message(message);
    }

    public void tellraw (String selector, Component message) {
        bot.core.run(String.format(
            "%s %s %s",
            "minecraft:tellraw",
            selector,
            GsonComponentSerializer.gson().serialize(message).trim()
        ));
    }

}