package land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation;

import static land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.crypto.Payload.withAdditionalData;
import static land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util.Constants.LOGGER;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;

import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.crypto.Payload;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.crypto.SigningManager;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.enums.SigningStatus;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util.SigningResult;


public class Signing {
    public static SigningResult getSigningStatus(String nick, String msg, byte[] pyl) {
        if (pyl == null) {
            return SigningResult.of(SigningManager.INSTANCE.doesNickRequireSignature(nick)
                ? SigningStatus.NEEDS_SIGNATURE
                : SigningStatus.NO_PAYLOAD);
        }

        final Payload decodedPayload;
        try {
            decodedPayload = Payload.read(
                msg.getBytes(StandardCharsets.UTF_8),
                new DataInputStream(new ByteArrayInputStream(pyl))
            );
        } catch (Exception e) {
            LOGGER.error("{}", Signing.class, e);
            return SigningResult.of(SigningStatus.NO_PAYLOAD);
        }

        final var response = SigningManager.INSTANCE.verifyClaim(
            withAdditionalData(decodedPayload.msg(), decodedPayload.nonce(), decodedPayload.exp()),
            decodedPayload.signature(),
            decodedPayload.key(),
            decodedPayload.exp()
        );

        return switch (response) {
            case SigningManager.Response.InvalidSignature  __ -> SigningResult.of(SigningStatus.INVALID_SIGNATURE);
            case SigningManager.Response.NoSuchKey         __ -> SigningResult.of(SigningStatus.NO_SUCH_KEY);
            case SigningManager.Response.ReusedSignature   __ -> SigningResult.of(SigningStatus.REUSED_SIGNATURE);
            case SigningManager.Response.SignatureValid valid -> {
                final boolean authenticated = valid.key().manifestUrlToClaims()
                    .values().stream()
                    .flatMap(Collection::stream)
                    .anyMatch(claim -> claim.equalsIgnoreCase(nick));

                final String encodedKey = Base64.getEncoder()
                    .encodeToString(valid.key().bc().getEncoded());

                yield new SigningResult(
                    authenticated ? SigningStatus.VERIFIED : SigningStatus.VALID_UNVERIFIED,
                    encodedKey
                );
            }
        };
    }
}