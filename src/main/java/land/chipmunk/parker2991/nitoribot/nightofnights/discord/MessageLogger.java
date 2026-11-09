package land.chipmunk.parker2991.nitoribot.nightofnights.discord;

import land.chipmunk.parker2991.nitoribot.nightofnights.Main;
import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.ListenerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MessageLogger extends ListenerAdapter {
    private JDA client;

    @Override
    public void onMessageReceived (MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(client.getSelfUser().getId())) return;

        for (NitoriBot bot : Main.Bots) {
            if (event.getChannel().getId().equals(bot.options.discordChannelId)) {
                for (Listener listener : bot.listenerManager.listeners) {
                    listener.discordMessageReceived(event);
                }
            }
        }
    }

    public MessageLogger (JDA client) {
       this.client = client;
    }
}
/*for (NitoriBot bot : Main.Bots) {
            if (!event.getChannel().getId().equals(bot.options.discordChannelId)) return;

            if (event.getAuthor().getId().equals(client.getSelfUser().getId())) return;

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

            bot.chat.tellraw(
                "@a",
                component
            );
        }*/