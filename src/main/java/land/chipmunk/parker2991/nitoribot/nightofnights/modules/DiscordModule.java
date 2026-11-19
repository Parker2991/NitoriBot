package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.Main;
import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandMessageSource;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandSource;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.chat.ChatType;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.login.clientbound.ClientboundLoginFinishedPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil.componentToAnsi;

public class DiscordModule extends ListenerAdapter implements Listener {
    private NitoriBot bot;
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordModule.class);
    private final List<String> discordQueue = new ArrayList<>();
    public JDA client = Main.discordClient;

    private Component chatTypes (
        ChatType chatType,
        Component component
    ) {
        return switch (chatType) {
            case ChatType.PLAYER -> Component.translatable(
                "[P] %s",
                component
            );
            case ChatType.DISGUISED -> Component.translatable(
                "[D] %s",
                component
            );
            case ChatType.SYSTEM -> Component.translatable(
                "[S] %s",
                component
            );
        };
    }

    @Override
    public void chatMessageReceived (
        ChatType chatType,
        Component component
    ) {
        final Component message = chatTypes(
            chatType,
            component
        );
        sendComponent(message);
    }

    private void sendDiscordMessage (String message) {
        discordQueue.add(message);
    }

    @Override
    public void discordMessageReceived (MessageReceivedEvent event) {
        if (!event
            .getChannel()
            .getId()
            .equals(bot.options.discordChannelId))
            return;

        if (event
            .getMessage()
            .getContentRaw()
            .startsWith(bot.config.discord.prefix)) {

            final String command = event
                .getMessage()
                .getContentRaw()
                .substring(bot.config.discord.prefix.length());
            final CommandSource source = new CommandSource(
                null,
                event,
                CommandMessageSource.DISCORD,
                null,
                null
            );

            bot.commandManager.executeString(
                source,
                command
            );
            return;
        }

        Component component = Component
            .translatable(
                "[%s] %s \u203a %s",
                Component.translatable(
                    "%s%s %s",
                    Component
                        .text("Nitori")
                        .color(NamedTextColor.BLUE),
                    Component
                        .text("Bot")
                        .color(NamedTextColor.GREEN),
                    Component
                        .text("Discord")
                        .color(NamedTextColor.DARK_BLUE)
                ),
                Component
                    .text(event
                        .getAuthor()
                        .getName())
                    .color(NamedTextColor.GRAY),
                Component
                    .text(event
                        .getMessage()
                        .getContentRaw())
                    .color(NamedTextColor.GRAY)
            )
            .color(NamedTextColor.AQUA);

        bot.chat.tellraw(
            "@a",
            component
        );
    }

    @Override
    public void packetReceived (Packet packet) {
        if (packet instanceof final ClientboundLoginFinishedPacket p) {
            if (!bot.config.discord.enabled) return;
            bot.scheduler.scheduleAtFixedRate(
                () -> {

                    if (discordQueue.isEmpty())
                        return;
                    try {
                        final String message = String.join(
                            "\n",
                            discordQueue
                        );

                        client
                            .getTextChannelById(bot.options.discordChannelId)
                            .sendMessage("```ansi\n" + message.substring(
                                0,
                                Math.min(
                                    message.length(),
                                    1984
                                )
                            ) + "\n```")
                            .queue();
                    } catch (Exception e) {
                        LOGGER.error(
                            "{}",
                            DiscordModule.class,
                            e
                        );
                    }
                    discordQueue.clear();
                },
                0,
                2000,
                TimeUnit.MILLISECONDS
            );
        }
    }

    private void sendComponent (
        Component component
    ) {
        final String message = componentToAnsi(component);

        try {
            sendDiscordMessage(message);
        } catch (Exception e) {
            LOGGER.error(
                "{}",
                DiscordModule.class,
                e
            );
        }
    }

    public DiscordModule (NitoriBot bot) throws InterruptedException {
        this.bot = bot;
        bot.listenerManager.addListener(this);
    }
}