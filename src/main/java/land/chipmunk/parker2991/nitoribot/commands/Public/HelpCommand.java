package land.chipmunk.parker2991.nitoribot.commands.Public;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.command.*;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.JoinConfiguration;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends CommandInfo {
  public HelpCommand () {
    super(
      "help",
      CommandTrustLevels.PUBLIC,
      new String[] { "heko", "hell", "?", "cmds"},
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
      .append(Component.text("\n"));

    return component;
  }

  @Override
  public CommandContext execute (CommandContext context) {
    Bot bot = context.bot;
    String[] args = context.args;
    CommandSource source = context.source;

    Component HelpLayout = Component.translatable(
      "%s: (%s) (%s | %s | %s | %s) \u203a\n",
      Component.text("Commands").color(NamedTextColor.BLUE),
      Component.text(bot.commandManager.commands.size()).color(NamedTextColor.GOLD),
      Component.text("Public").color(NamedTextColor.BLUE),
      Component.text("Trusted").color(NamedTextColor.DARK_AQUA),
      Component.text("Admin").color(NamedTextColor.AQUA),
      Component.text("Owner").color(NamedTextColor.LIGHT_PURPLE)
    ).color(NamedTextColor.GRAY);

    Component component = Component.empty()
      .append(HelpLayout)
      .append(Component.join(JoinConfiguration.separator(Component.space()), getCommands(bot)));
    
//    System.out.println(ComponentUtil.componentToAnsi(component));

    source.sendFeedback(component);
    return null;
  }
}
