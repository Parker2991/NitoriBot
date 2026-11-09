package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.ChatAttestation;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.MessagingEntrypointImpl;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util.SigningResult;
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
import java.util.*;

public class ChatModule implements Listener {
    public final List<ParseChatData> chatParsers;
    private final NitoriBot bot;

    public ChatModule (NitoriBot bot) {
        this.bot = bot;

        this.chatParsers = List.of(new KaboomChatParser(bot));

        bot.listenerManager.addListener(this);
    }

    @Override
    public void packetReceived (Packet packet) {
        MessagingEntrypointImpl messagingEntrypoint = new MessagingEntrypointImpl();
        switch (packet) {
            case ClientboundSystemChatPacket p -> systemChat(p);
            case ClientboundPlayerChatPacket p -> playerChat(messagingEntrypoint, p);
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
            listener.chatReceived(ChatType.SYSTEM, message);
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

        UUID uuid = bot.players.getDisplayName(targetUsername).profile.getId();
        String username = bot.players.getDisplayName(targetUsername).profile.getName();

        /*SigningStatus result = ChatAttestation.getSigningStatus(
            ComponentUtil.componentToString(message),
            bot.players.getDisplayName(targetUsername).profile.getId(),

        );*/

        for (Listener listener : bot.listenerManager.listeners) {
            listener.chatReceived(ChatType.DISGUISED, message);
            listener.disguisedChatReceived(message);
        }

        parseMessage(
            ChatType.DISGUISED, message, new PlayerMessageData(
                bot.players.getPlayerByUsername(ComponentUtil.componentToString(packet.getName())),
                packet.getMessage(),
                ChatType.DISGUISED,
                packet.getName()
            )
        );

    }

    public void playerChat (MessagingEntrypointImpl messagingEntrypoint, ClientboundPlayerChatPacket packet) {
        final Component unsignedContent = packet.getUnsignedContent();
        final Component content = Component.text(packet.getContent());

        /*SigningResult result = ChatAttestation.getSigningStatus(
            packet.getContent(),
            packet.getSender(),
            ComponentUtil.componentToString(packet.getName())
        );*/

        //bot.chat.tellraw("@a", Component.text(result.toString()));

        for (Listener listener : bot.listenerManager.listeners) {
            listener.chatReceived(ChatType.PLAYER, unsignedContent);
            listener.playerChatReceived(unsignedContent);
            //listener.chatReceived(ChatType.PLAYER, result.status(), unsignedContent);
        }

        parseMessage(
            ChatType.PLAYER, unsignedContent, new PlayerMessageData(
                bot.players.getPlayerUUID(packet.getSender()),
                content,
                ChatType.PLAYER,
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
                    final PlayerMessageData playerMessageData = new PlayerMessageData(
                        parsed.sender(),
                        parsed.contents(),
                        ChatType.DISGUISED,
                        data.senderName()
                    );
                    listener.parsedMessage(playerMessageData);
                } else listener.parsedMessage(data);
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