package land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.enums;

public enum SigningStatus {
    NO_PAYLOAD,
    NEEDS_SIGNATURE,
    INVALID_SIGNATURE,
    NO_SUCH_KEY,
    REUSED_SIGNATURE,
    VERIFIED,        // authenticated via manifest
    VALID_UNVERIFIED // valid sig, but key not authorized for nick
}