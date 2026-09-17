package land.chipmunk.parker2991.nitoribot.commands.Public;

import land.chipmunk.parker2991.nitoribot.command.CommandContext;
import land.chipmunk.parker2991.nitoribot.command.CommandInfo;
import land.chipmunk.parker2991.nitoribot.command.CommandTrustLevels;
import land.chipmunk.parker2991.nitoribot.data.buildstring.BotBuildInfo;
import land.chipmunk.parker2991.nitoribot.data.buildstring.RepoCommitInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class InfoCommand extends CommandInfo {
  public InfoCommand () {
    super(
      "info",
      CommandTrustLevels.PUBLIC,
      new String[]{ "information" },
      "information about the bot"
    );
  }

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
      case "version" -> {
        BotBuildInfo info = bot.botBuildInfo;

        RepoCommitInfo repoCommitInfo = bot.repoCommitInfo;

        ZonedDateTime dateTime = ZonedDateTime.parse(repoCommitInfo.created);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm:ss");

        component = Component.translatable(
          "%s-%s-%s\n%s: %s\n%s: %s\n%s: %s",
          gson.deserialize(info.buildstring.botName.toString()),
          Component.text(info.buildstring.version).color(NamedTextColor.AQUA),
          gson.deserialize(info.buildstring.codename.toString()),
          Component.text("Build").color(NamedTextColor.BLUE),
          Component.text(info.buildstring.build).color(NamedTextColor.YELLOW),
          Component.text("Version release Date").color(NamedTextColor.BLUE),
          Component.text(dateTime.format(dateTimeFormatter)).color(NamedTextColor.DARK_BLUE),
          Component.text("Commit").color(NamedTextColor.BLUE),
          Component.text(repoCommitInfo.sha.substring(0, 8)).color(NamedTextColor.DARK_BLUE)
        ).color(NamedTextColor.GRAY);
      }
    }

    source.sendFeedback(component);
  }
}
