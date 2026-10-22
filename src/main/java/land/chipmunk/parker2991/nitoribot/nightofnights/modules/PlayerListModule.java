package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.Bot;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.PlayerProfileData;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.cloudburstmc.math.vector.Vector3d;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntry;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntryAction;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerInfoRemovePacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundPlayerInfoUpdatePacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundAddEntityPacket;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class PlayerListModule implements Listener {
    private final Bot bot;
    public final List<PlayerProfileData> list = new ArrayList<>();


    public PlayerListModule (Bot bot) {
        this.bot = bot;
        bot.listenerManager.addListener(this);
    }

    @Override
    public void packetReceived (Packet packet) {
        switch (packet) {
            case ClientboundPlayerInfoUpdatePacket p -> playerInfo(p);
            case ClientboundPlayerInfoRemovePacket p -> playerRemove(p);
            case ClientboundAddEntityPacket p -> getPlayerPosition(p);
            case ClientboundSystemChatPacket p -> systemChatReceived(p);
            default -> {
            }
        }
    }

    public void systemChatReceived (ClientboundSystemChatPacket packet) {
        try {
            if (!(packet.getContent() instanceof TextComponent component)) return;
           // if (component.insertion().equals("nitoribot.player.ip")) {

            //}
        } catch (Exception ignore) {
        }
    }

    public void playerInfo (ClientboundPlayerInfoUpdatePacket packet) {
        EnumSet<PlayerListEntryAction> actions = packet.getActions();

        for (PlayerListEntryAction action : actions) {
            for (PlayerListEntry entry : packet.getEntries()) {
                switch (action) {
                    case PlayerListEntryAction.UPDATE_LIST_ORDER, PlayerListEntryAction.UPDATE_HAT,
                         PlayerListEntryAction.INITIALIZE_CHAT, PlayerListEntryAction.ADD_PLAYER -> addPlayer(entry);
                    case PlayerListEntryAction.UPDATE_LISTED -> updateListed(entry);
                    case PlayerListEntryAction.UPDATE_GAME_MODE -> updateGamemode(entry);
                    case PlayerListEntryAction.UPDATE_LATENCY -> updateLatency(entry);
                    case PlayerListEntryAction.UPDATE_DISPLAY_NAME -> updateDisplayName(entry);
                }
            }
        }
    }

    public void getPlayerPosition (ClientboundAddEntityPacket packet) {
        if (packet.getType() == EntityType.PLAYER) {
            UUID uuid = packet.getUuid();
            int entityId = packet.getEntityId();
            Vector3d playerPosition = Vector3d.from(
                packet.getX(),
                packet.getY(),
                packet.getZ()
            );
            PlayerProfileData player = getPlayerUUID(uuid);
            if (player != null) {
                player.position = playerPosition;
                player.entityId = entityId;
            }
        }
    }

    @Nullable
    public final PlayerProfileData getPlayerUUID (UUID uuid) {
        PlayerProfileData playerInfo = null;

        try {
            // lazy ass fix for the bot erroring when a player is null
            for (PlayerProfileData player : list) {
                UUID playerUUID = player.profile.getId();

                if (playerUUID == null) return playerInfo;
                else if (player.profile.getId().equals(uuid)) {
                    playerInfo = player;
                }
            }
        } catch (Exception _) {
        }
        return playerInfo;
    }

    public final PlayerProfileData getDisplayName (Component displayName) {
        for (PlayerProfileData candidate : list) {
            if (candidate.displayName != null && candidate.displayName.equals(displayName)) {
                return candidate;
            }
        }

        return null;
    }

    public final PlayerProfileData getPlayerByUsername (String username) {
        for (PlayerProfileData candidate : list) {
            if (candidate.profile.getName().equals(username)) {
                return candidate;
            }
        }

        return null;
    }

    public void updateListed (PlayerListEntry player) {
        PlayerProfileData getPlayer = getPlayerUUID(player.getProfileId());

        if (getPlayer == null) return;

        getPlayer.listed = player.isListed();
    }

    public void updateGamemode (PlayerListEntry player) {
        PlayerProfileData getPlayer = getPlayerUUID(player.getProfileId());

        if (getPlayer == null) {
        } else getPlayer.gameMode = player.getGameMode();
    }

    public void updateLatency (PlayerListEntry player) {
        PlayerProfileData getPlayer = getPlayerUUID(player.getProfileId());

        if (getPlayer == null) {
        } else getPlayer.latency = player.getLatency();
    }

    public void updateDisplayName (PlayerListEntry player) {
        PlayerProfileData getPlayer = getPlayerUUID(player.getProfileId());

        if (getPlayer == null) {
        } else getPlayer.displayName = player.getDisplayName();
    }

    private void getPlayerIP (UUID uuid) {
        /*
        this function is unused right now because i cant figure how the fuck to get ips without problems
        first i tried packets which didnt work half the time
        now i tried tellraw but thats gonna cause problems because its gonna spam a servers console quite a lot on bot and player joins
        feel free to reach out to me on matrix or discord, hell even a issue on the bots repo for this
         */
        final String selector = String.format(
            "@p[nbt={UUID:uuid(\"%s\")}]",
            bot.profile.getIdAsString()
        );

        bot.chat.tellraw(
            selector,
            bot.core.runTrackedWithTellraw(
                "nitoribot.player.ip",
                uuid.toString(),
                String.format(
                    "essentials:seen %s",
                    uuid
                )
            )
        );
    }

    public void addPlayer (PlayerListEntry player) {
        PlayerProfileData findDuplicatePlayer = getPlayerUUID(player.getProfileId());

        if (findDuplicatePlayer != null) list.remove(findDuplicatePlayer);
        PlayerProfileData formatPlayerData = new PlayerProfileData(
            player.getProfileId(),
            null,
            player.getProfile(),
            player.isListed(),
            player.getLatency(),
            player.getGameMode(),
            player.getDisplayName(),
            player.isShowHat(),
            player.getListOrder(),
            null,
            null,
            0
        );

        list.add(formatPlayerData);
    }

    public void playerRemove (ClientboundPlayerInfoRemovePacket packet) {
        try {
            for (UUID uuid : packet.getProfileIds()) {
                PlayerProfileData target = getPlayerUUID(uuid);
                bot.tabComplete.completion("scoreboard players add ").thenApply((suggestion) -> {
                    final String[] matches = suggestion.getMatches();
                    final Component[] tooltips = suggestion.getTooltips();
                    final String username = target.profile.getName();

                    for (int i = 0; i < matches.length; i++) {
                        if (tooltips[i] != null || !matches[i].equals(username)) continue;
                        target.listed = false;
                        return suggestion;
                    }

                    list.remove(target);

                    return suggestion;
                });
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}