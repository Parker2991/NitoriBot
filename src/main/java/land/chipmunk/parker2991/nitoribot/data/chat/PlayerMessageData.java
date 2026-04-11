package land.chipmunk.parker2991.nitoribot.data.chat;

import net.kyori.adventure.text.Component;

import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;

public class PlayerMessageData {
  public PlayerProfileData sender;
  public Component contents;
  public String chatType;
  public Component senderName;

  public PlayerMessageData(PlayerProfileData sender, Component contents, String chatType, Component senderName) {
    this.sender = sender;
    this.contents = contents;
    this.chatType = chatType;
    this.senderName = senderName;
   // return this;
  }
}
