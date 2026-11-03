package land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.crypto;

import com.google.common.primitives.Longs;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.config.ConfigurationManager;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorInputStream;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

import static land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.crypto.SigningManager.PERIOD;
import static land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util.Constants.LOGGER;
import static land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util.Constants.MESSAGE_LIMIT;

public record Payload(byte[] msg, byte[] signature, byte[] key, byte[] nonce, long exp, boolean compressedMsg) {
    private static final int ORIGINAL_MSG_BUDGET = (MESSAGE_LIMIT - 4) - 64 - 16 - 8 - 2 - 1; // 64 for signature size, 16 for key hash, 8 for exp, 2 for nonce, 1 for payload header

    public static Payload read(byte[] msg, DataInputStream input) throws IOException {
        final int replacementMsgBytes = input.readUnsignedByte();
        final boolean isCompressed = replacementMsgBytes != 0;
        if (isCompressed) {
            msg = new byte[replacementMsgBytes];
            input.readFully(msg);
        }
        final byte[] signature = new byte[64];
        input.readFully(signature);
        final byte[] keyHash = new byte[16];
        input.readFully(keyHash);
        final byte[] nonce = new byte[2];
        input.readFully(nonce);
        return new Payload(msg, signature, keyHash, nonce, input.readLong(), isCompressed);
    }

    public static byte[] withAdditionalData(byte[] msg, byte[] nonce, long exp) {
        final byte[] msgWithNonceAndExp = new byte[msg.length + 2 + 8];
        System.arraycopy(nonce, 0, msgWithNonceAndExp, 0, 2);
        System.arraycopy(Longs.toByteArray(exp), 0, msgWithNonceAndExp, 2, 8);
        System.arraycopy(msg, 0, msgWithNonceAndExp, 2 + 8, msg.length);
        return msgWithNonceAndExp;
    }

    public static boolean write(byte[] msg, DataOutput output) throws IOException {
        final boolean compressed = msg.length > ORIGINAL_MSG_BUDGET || ConfigurationManager.INSTANCE.config.forceCompress;

        if (compressed) {
            final var out = new ByteArrayOutputStream();
            final var cmp = new ZstdCompressorOutputStream(out);
            cmp.write(msg);
            cmp.flush();
            cmp.close();
            msg = out.toByteArray();
        }

        output.writeByte(compressed ? msg.length : 0);
        if (compressed) output.write(msg);
        final byte[] nonce = new byte[2];
        ThreadLocalRandom.current().nextBytes(nonce);
        final long exp = System.currentTimeMillis() + PERIOD;
        msg = withAdditionalData(msg, nonce, exp);
        final var signature = SigningManager.INSTANCE.createSignature(msg);
        output.write(signature);
        output.write(SigningManager.INSTANCE.selfHash);
        output.write(nonce);
        output.writeLong(exp);
        return compressed;
    }

    public String getReplacementMsg() throws IOException {
        if (!this.compressedMsg) return new String(this.msg, StandardCharsets.UTF_8);
        @SuppressWarnings("resource") final var in = new ZstdCompressorInputStream(new ByteArrayInputStream(this.msg));
        final var out = new ByteArrayOutputStream();

        final byte[] buf = new byte[8192];
        int read;
        while ((read = in.read(buf)) != -1) {
            if (out.size() >= ConfigurationManager.INSTANCE.config.maxCompressedPayload)
                return new String(this.msg, StandardCharsets.UTF_8);

            out.write(buf, 0, read);
        }
        return out.toString(StandardCharsets.UTF_8);
    }
}
