package land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util;

import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.enums.SigningStatus;

public record SigningResult(SigningStatus status, String encodedKey) {
    public static SigningResult of(SigningStatus status) {
        return new SigningResult(status, null);
    }
}