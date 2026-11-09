package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.Main;
import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandMessageSource;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandSource;
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

import static land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil.componentToAnsi;

public class DiscordModule extends ListenerAdapter implements Listener {
    private NitoriBot bot;
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordModule.class);
    private final List<String> discordQueue = new ArrayList<>();
    public JDA client = Main.discordClient;

    @Override
    public void systemChatReceived (Component message) {
        sendComponent(message);
    }

    @Override
    public void playerChatReceived (Component message) {
        sendComponent(message);
    }

    @Override
    public void disguisedChatReceived (Component message) {
        sendComponent(message);
    }

    private void sendDiscordMessage (String message) {
        discordQueue.add(message);
    }

    @Override
    public void discordMessageReceived (MessageReceivedEvent event) {
        if (!event.getChannel().getId().equals(bot.options.discordChannelId)) return;

        if (event.getMessage().getContentRaw().startsWith(bot.config.discord.prefix)) {

            final String command = event.getMessage().getContentRaw().substring(bot.config.discord.prefix.length());
            final CommandSource source = new CommandSource(null, event, CommandMessageSource.DISCORD, null, null);

            bot.commandManager.executeString(source, command);
            return;
        }
        /*
          PlayerProfileData sender,
    MessageReceivedEvent discordEvent,
    CommandMessageSource source,
    ChatType chatType,
    ParsedChatType parsedChatType
         */

        Component component = Component.translatable(
            "[%s] %s \u203a %s",
            Component.translatable(
                "%s%s %s",
                Component.text("Nitori").color(NamedTextColor.BLUE),
                Component.text("Bot").color(NamedTextColor.GREEN),
                Component.text("Discord").color(NamedTextColor.DARK_BLUE)
            ),
            Component.text(event.getAuthor().getName()).color(NamedTextColor.GRAY),
            Component.text(event.getMessage().getContentRaw()).color(NamedTextColor.GRAY)
        ).color(NamedTextColor.AQUA);

        bot.chat.tellraw("@a", component);
    }

    @Override
    public void packetReceived (Packet packet) {
        if (packet instanceof final ClientboundLoginFinishedPacket p) {
            Timer timer = new Timer();

            timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run () {
                        if (discordQueue.isEmpty()) return;
                        try {
                            final String message = String.join("\n", discordQueue);

                            client.getTextChannelById(bot.options.discordChannelId)
                                .sendMessage("```ansi\n" + message.substring(
                                    0,
                                    Math.min(message.length(), 1984)
                                ) + "\n```")
                                .queue();
                        } catch (Exception e) {
                            LOGGER.error("{}", DiscordModule.class, e);
                        }
                        discordQueue.clear();
                    }
                }, 2000, 2000
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
            LOGGER.error("{}", DiscordModule.class, e);
        }
    }

    public DiscordModule (NitoriBot bot) throws InterruptedException {
        this.bot = bot;
        bot.listenerManager.addListener(this);
    }
}