package land.chipmunk.parker2991.nitoribot.commands.Public;

import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandTrustLevels;
import land.chipmunk.parker2991.nitoribot.data.buildstring.BotBuildInfo;
import land.chipmunk.parker2991.nitoribot.data.buildstring.RepoCommitInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class InfoCommand extends CommandInfo {
  public InfoCommand () throws IOException {
    super(
      "info",
      CommandTrustLevels.PUBLIC,
      new String[]{ "information" },
      "information about the bot"
    );
  }

  private final Enumeration<URL> resources = InfoCommand.class.getClassLoader().getResources("META-INF/MANIFEST.MF");

  private final Manifest manifest = new Manifest(resources.nextElement().openStream());

  private final Attributes attributes = manifest.getMainAttributes();

  private final String buildTime = attributes.getValue("Build-Time");

  @Override
  public void execute (CommandContext context) {
    var bot = context.bot;

    var source = context.source;

    var args = context.args;

    Component component = null;
/*
Build: 3355
Repo Build: 242
Version Release Date: 10/22/2025, 11:24:04 AM
Commit: fcbd9f983c
11/22/22-9/10/2026
 */
    GsonComponentSerializer gson = GsonComponentSerializer.gson();

    switch (args[0].toLowerCase()) {
      case "loaded" -> {
        component = Component.translatable(
          "%s: %s\n%s: %s\n%s: %s",
          Component.text("Modules").color(NamedTextColor.BLUE),
          Component.text(bot.modules.size()).color(NamedTextColor.GOLD),
          Component.text("Commands").color(NamedTextColor.BLUE),
          Component.text(bot.commandManager.commands.size()).color(NamedTextColor.GOLD),
          Component.text("Chat Parsers").color(NamedTextColor.BLUE),
          Component.text(bot.chat.chatParsers.size()).color(NamedTextColor.GOLD)
        );
      }

      case "version" -> {
        BotBuildInfo info = bot.botBuildInfo;

        RepoCommitInfo repoCommitInfo = bot.repoCommitInfo;

        ZonedDateTime dateTime = ZonedDateTime.parse(repoCommitInfo.created);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm:ss a");

        component = Component.translatable(
          "%s-%s-%s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",
          gson.deserialize(info.buildstring.botName.toString()),
          Component.text(info.buildstring.version).color(NamedTextColor.AQUA),
          gson.deserialize(info.buildstring.codename.toString()),
          Component.text("Build").color(NamedTextColor.BLUE),
          Component.text(info.buildstring.build).color(NamedTextColor.YELLOW),
          Component.text("Commit").color(NamedTextColor.BLUE),
          Component.text(repoCommitInfo.sha.substring(0, 8)).color(NamedTextColor.DARK_BLUE),
          Component.text("Version release Date").color(NamedTextColor.BLUE),
          Component.text(dateTime.format(dateTimeFormatter)).color(NamedTextColor.DARK_BLUE),
          Component.text("Initial Bot Release").color(NamedTextColor.BLUE),
          Component.text(info.buildstring.initialBotRelease).color(NamedTextColor.DARK_BLUE),
          Component.text("Jar Compile Date").color(NamedTextColor.BLUE),
          Component.text(buildTime).color(NamedTextColor.DARK_BLUE)
        ).color(NamedTextColor.GRAY);
      }
    }

    source.sendFeedback(component);
  }
}