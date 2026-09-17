package land.chipmunk.parker2991.nitoribot.commands.Public;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.Config;
import land.chipmunk.parker2991.nitoribot.command.*;
import land.chipmunk.parker2991.nitoribot.util.TranslationsUtil;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.TextColor;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpCommand extends CommandInfo {
  public HelpCommand () {
    super(
      "help",
      CommandTrustLevels.PUBLIC,
      new String[]{ "heko", "hell", "?", "cmds" },
      "see the list of commands"
    );
  }


  public Component getCommands (Bot bot) {
    List<Component> commandlist = new ArrayList<>();
    List<Component> Public = new ArrayList<>();
    List<Component> Trusted = new ArrayList<>();
    List<Component> Admin = new ArrayList<>();
    List<Component> Owner = new ArrayList<>();

    for (CommandInfo commands : bot.commandManager.commands) {
      switch (commands.trustlevel) {
        case CommandTrustLevels.PUBLIC:
          Public.add(
            Component.text(commands.name + " ").color(NamedTextColor.BLUE)
          );
          break;
        case CommandTrustLevels.TRUSTED:
          Trusted.add(
            Component.text(commands.name + " ").color(NamedTextColor.DARK_AQUA)
          );
          break;
      }
    }

    Component component = Component.empty()
      .append(Public)
      .append(Component.text("\n"))
      .append(Trusted);
    // .append(Component.text("\n"));

    return component;
  }

  private Component getCommand (Bot bot, String[] command) {
    List<Component> list = new ArrayList<>();

    Component component = null;

    for (CommandInfo commands : bot.commandManager.commands) {
      for (String aliases : commands.aliases) {
        if (commands.name.equals(command[0]) || aliases.equals(command[0])) component = Component.empty()
          .append(
            TranslationsUtil.customTranslations(
              "command.help.info.layout",
              List.of(
                TranslationsUtil.customTranslations("command.help.info.command", null).color(NamedTextColor.BLUE),
                Component.text(commands.name).color(NamedTextColor.AQUA)
              )
            )
          )
          .append(
            TranslationsUtil.customTranslations(
              "command.help.info.layout",
              List.of(
                TranslationsUtil.customTranslations("command.help.info.aliases", null).color(NamedTextColor.BLUE),
                Component.text(Arrays.toString(commands.aliases)).color(NamedTextColor.AQUA)
              )
            )
          )
          .append(
            TranslationsUtil.customTranslations(
              "command.help.info.layout",
              List.of(
                TranslationsUtil.customTranslations("command.help.info.description", null).color(NamedTextColor.BLUE),
                Component.text(commands.description).color(NamedTextColor.AQUA)
              )
            )
          )
          .append(
            TranslationsUtil.customTranslations(
              "command.help.info.layout",
              List.of(
                TranslationsUtil.customTranslations("command.help.info.trust_level", null).color(NamedTextColor.BLUE),
                Component.text(commands.trustlevel + "").color(NamedTextColor.AQUA)
              )
            )
          )
          .append(
            TranslationsUtil.customTranslations(
              "command.help.info.layout",
              List.of(
                TranslationsUtil.customTranslations("command.help.info.usages", null).color(NamedTextColor.BLUE),
                Component.text("DUMMY String").color(NamedTextColor.AQUA)
              )
            )
          );
      }
    }
    return component;
  }

  @Override
  public void execute (CommandContext context) {
    Bot bot = context.bot;
    String[] args = context.args;
    CommandSource source = context.source;
    Config config = bot.config;
    Config.Colors colors = config.colors;
    List<Component> list = new ArrayList<>();

    if (args.length != 0) {
      source.sendFeedback(getCommand(bot, args));
      return;
    }

    list = List.of(
      Component.text("Commands").color(NamedTextColor.NAMES.value(colors.commands.primary)),
      Component.text(bot.commandManager.commands.size()).color(NamedTextColor.NAMES.value(colors.integer)),
      TranslationsUtil.customTranslations("command.help.trust_level.public", null).color(NamedTextColor.NAMES.value(colors.help.Public)),
      TranslationsUtil.customTranslations("command.help.trust_level.trusted", null).color(NamedTextColor.NAMES.value(colors.help.trusted)),
      TranslationsUtil.customTranslations("command.help.trust_level.admin", null).color(NamedTextColor.NAMES.value(colors.help.admin)),
      TranslationsUtil.customTranslations("command.help.trust_level.owner", null).color(NamedTextColor.NAMES.value(colors.help.owner))
    );

    Component helpLayout = TranslationsUtil.customTranslations(
      "command.help.layout",
      list
    );

    Component component = Component.empty()
      .append(helpLayout)
      .append(Component.join(JoinConfiguration.separator(Component.space()), getCommands(bot)));

    source.sendFeedback(component);
  }
}
