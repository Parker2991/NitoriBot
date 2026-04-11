package land.chipmunk.parker2991.nitoribot.command;

import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.Bot;

import net.kyori.adventure.text.Component;

public class CommandSource {
  private Bot bot;

  public PlayerProfileData sender;

  public boolean inGame;

  public CommandSource (Bot bot, PlayerProfileData sender) {
    this.bot = bot;
    this.sender = sender;
  }


  public void sendFeedback (Component message) {
    String selector = String.format(
      "@p[nbt={UUID:%s}]",
      sender.entityUUID
    );

    bot.chat.tellraw(selector, message);
  }

}