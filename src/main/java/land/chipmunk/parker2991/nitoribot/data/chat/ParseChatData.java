package land.chipmunk.parker2991.nitoribot.data.chat;

//import land.chipmunk.parker2991.nitoribot.data.PlayerProfileData;

import net.kyori.adventure.text.Component;

//import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntry;

public interface ParseChatData {
  PlayerMessageData parse (Component message);

 // PlayerProfileData parse (PlayerMessageData data);
}
/*
package land.chipmunk.chipmunkbot.data.chat;

import net.kyori.adventure.text.Component;

public interface SystemChatParser {
  PlayerMessage parse (Component message);
}
*/