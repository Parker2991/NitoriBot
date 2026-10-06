package land.chipmunk.parker2991.nitoribot.data;

import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record PlayerProfileData(
    UUID uuid,
    String entityUUID,
    GameProfile profile,
    boolean listed,
    int latency,
    GameMode gameMode,
    Component displayName,
    boolean showHat,
    int listOrder,
    PositionData position,
    String dimension,
    Integer entityId
) {
    public void listed (boolean listed) {
    }

    public void gameMode (GameMode gameMode) {
    }

    public void latency (int latency) {
    }
    
    public void position (PositionData position) {
    } 

    public void displayName (@Nullable Component displayName) {
    }

    public void entityId (int entityId) {

    }
}