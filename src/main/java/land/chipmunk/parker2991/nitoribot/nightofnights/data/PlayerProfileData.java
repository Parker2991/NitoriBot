package land.chipmunk.parker2991.nitoribot.nightofnights.data;


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

    public PlayerProfileData (
        UUID uuid,
        String entityUUID,
        GameProfile profile,
        boolean listed,
        int latency,
        GameMode gameMode,
        Component displayName,
        boolean showHat,
        int listOrder,
        Vector3d position,
        String dimension,
        int entityId
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