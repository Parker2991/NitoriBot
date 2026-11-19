package land.chipmunk.parker2991.nitoribot.nightofnights.data;


import land.chipmunk.parker2991.nitoribot.nightofnights.command.CommandTrustLevels;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3d;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;

import java.util.UUID;

public class PlayerProfileData {
    public UUID uuid;
    public String entityUUID;
    public GameProfile profile;
    public boolean listed;
    public int latency;
    public GameMode gameMode;
    public Component displayName;
    public boolean showHat;
    public int listOrder;
    public Vector3d position;
    public String dimension;
    public int entityId;
    public CommandTrustLevels trustLevel = CommandTrustLevels.PUBLIC;
    public boolean alreadyValidated = false;

    public PlayerProfileData (
        final UUID uuid,
        final String entityUUID,
        final GameProfile profile,
        final boolean listed,
        final int latency,
        final GameMode gameMode,
        final Component displayName,
        final boolean showHat,
        final int listOrder,
        final Vector3d position,
        final String dimension,
        final int entityId
    ) {
        this.uuid = uuid;
        this.entityUUID = entityUUID;
        this.profile = profile;
        this.listed = listed;
        this.latency = latency;
        this.gameMode = gameMode;
        this.displayName = displayName;
        this.showHat = showHat;
        this.listOrder = listOrder;
        this.position = position;
        this.dimension = dimension;
        this.entityId = entityId;
    }
}