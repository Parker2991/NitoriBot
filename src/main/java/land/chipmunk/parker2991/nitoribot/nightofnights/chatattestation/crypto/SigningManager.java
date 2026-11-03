package land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.crypto;

import com.google.gson.reflect.TypeToken;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.KeyManifest;
import land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.config.ConfigurationManager;
import net.jodah.expiringmap.ExpiringMap;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.jetbrains.annotations.Unmodifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

import static land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util.Constants.*;

public final class SigningManager {
    public static final Logger LOGGER = LoggerFactory.getLogger(SigningManager.class);
    public static final SigningManager INSTANCE = new SigningManager();
    public static final long PERIOD = 10_000L;
    public final byte[] selfHash;
    private final ExecutorService requestExecutor = Executors.newCachedThreadPool();
    private final StampedLock lock = new StampedLock();
    private final Map<String, KeyManifest> manifests;
    private final Set<String> nicks = new HashSet<>();
    private final Map<WrappedByteArray, LoadedKey> hashToKey = new HashMap<>();
    private final Map<Long, Set<WrappedByteArray>> usedSignatureMap = ExpiringMap.builder()
            .expiration(PERIOD, TimeUnit.MILLISECONDS)
            .build();
    private final Ed25519PrivateKeyParameters selfKey;
    private final Path cachePath;

    public SigningManager() {
        this.cachePath = storageDir.resolve("data").resolve("chatattestation-cache.json");

        final var cacheParent = this.cachePath.getParent();
        final boolean orphan = Files.notExists(cacheParent);

        try {
            if (orphan) {
                LOGGER.info("Creating Chat Attestation folder");
                Files.createDirectories(cacheParent);
            }

            if (orphan || Files.notExists(this.cachePath)) {
                this.manifests = new HashMap<>();
            } else {
                this.manifests = GSON.fromJson(
                        Files.newBufferedReader(this.cachePath),
                        new TypeToken<>() {

                        }
                );
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to initialize cache", e);
        }

        final var legacyKeyPath = storageDir.resolve("chatattestation.key");
        final var keyPath = storageDir.resolve("chatattestation.priv.key");

        if (Files.exists(legacyKeyPath)) {
            try {
                Files.move(legacyKeyPath, keyPath);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to rename legacy key path");
            }
        }

        if (Files.notExists(keyPath)) {
            try {
                this.selfKey = new Ed25519PrivateKeyParameters(SecureRandom.getInstanceStrong());
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalArgumentException("JVM does not provide strong secure random impl", e);
            }

            this.savePrivateKey(keyPath);
        } else {
            final byte[] keyData;

            try {
                keyData = Files.readAllBytes(keyPath);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to load stored private key", e);
            }
            if (keyData.length == 32) {
                this.selfKey = new Ed25519PrivateKeyParameters(keyData);
                this.savePrivateKey(keyPath);
            } else {
                final var extractedData = Base64.getDecoder().decode(
                        new String(keyData, StandardCharsets.UTF_8)
                                .lines()
                                .filter(l -> !l.startsWith("-"))
                                .map(String::trim)
                                .collect(Collectors.joining())
                );

                try {
                    this.selfKey = (Ed25519PrivateKeyParameters) PrivateKeyFactory.createKey(extractedData);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Invalid private key", e);
                }
            }
           /* LOGGER.info(
                "Encoded public key: {}",
                Base64.getEncoder().encodeToString(this.selfKey.generatePublicKey().getEncoded())
            );*/
        }

        final var encodedPublicKey = this.selfKey.generatePublicKey().getEncoded();
        this.selfHash = createHash(encodedPublicKey);

        @SuppressWarnings("resource") final var scheduler = Executors.newSingleThreadScheduledExecutor(DAEMON_THREAD_FACTORY);
        scheduler.scheduleAtFixedRate(this::refetchKeys, 0, 1, TimeUnit.MINUTES);
    }

    public static void init() {

    }

    public static byte[] createHash(byte[] input) {
        final MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA3-224");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("JVM does not have SHA3-224 support", e);
        }

        return Arrays.copyOfRange(messageDigest.digest(input), 0, 16);
    }

    private static long getCurrentPeriod() {
        return 0;
    }

    private void savePrivateKey(Path keyPath) {
        try (final var out = Files.newBufferedWriter(keyPath)) {
            out.write("-----BEGIN PRIVATE KEY-----\n");

            final var encoded = Base64.getEncoder().encodeToString(
                    PrivateKeyInfoFactory.createPrivateKeyInfo(
                            this.selfKey
                    ).getEncoded()
            );
            out.write(encoded);
            out.write("\n-----END PRIVATE KEY-----");
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save private key", e);
        }
    }

    private CompletableFuture<KeyManifest> refetchManifest(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try (final var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30)).build()) {
                final var request = client.send(
                        HttpRequest.newBuilder(URI.create(url)).GET().build(),
                        HttpResponse.BodyHandlers.ofString()
                );

                return GSON.fromJson(request.body(), KeyManifest.class);
            } catch (Exception e) {
               throw new CompletionException("Failed to fetch manifest", e);
            }
        }, this.requestExecutor);
    }

    public void refetchKeys() {
        final var futures = ConfigurationManager.INSTANCE.config.keyManifestUrls.stream()
                .collect(Collectors.toMap(url -> url, this::refetchManifest));

        for (final var futureEntry : futures.entrySet()) {
            final String manifest = futureEntry.getKey();

            try {
                this.manifests.put(manifest, futureEntry.getValue().join());
            } catch (Exception e) {
                LOGGER.warn("Fetching {} failed, cache will be used if present", manifest, e);
            }
        }

        try (final var writer = Files.newBufferedWriter(this.cachePath)) {
            GSON.toJson(this.manifests, writer);
            writer.flush();
        } catch (IOException e) {
            LOGGER.warn("Failed to save manifest cache", e);
        }

        this.reloadKeys();
    }

    private void reloadKeys() {
        final long writeLock = this.lock.writeLock();

        try {
            this.hashToKey.clear();
            this.nicks.clear();

            for (final var entry : this.manifests.entrySet()) {
                final var manifestUrl = entry.getKey();

                for (final var keyToClaim : entry.getValue().keyToClaims().entrySet()) {
                    final var rawKey = Base64.getDecoder()
                            .decode(keyToClaim.getKey());
                    final var loadedKey = this.hashToKey.computeIfAbsent(
                            new WrappedByteArray(
                                    createHash(rawKey)
                            ),
                            k -> new LoadedKey(
                                    new Ed25519PublicKeyParameters(rawKey),
                                    new HashMap<>()
                            )
                    );

                    final var keyClaims = keyToClaim.getValue();
                    final var claims = loadedKey.manifestUrlToClaims.computeIfAbsent(manifestUrl, k -> new HashSet<>());
                    claims.addAll(keyClaims);

                    for (final var claim : keyClaims)
                        this.nicks.add(claim.toLowerCase());
                }
            }
        } finally {
            this.lock.unlockWrite(writeLock);
        }
    }

    public Response verifyClaim(byte[] msg, byte[] sig, byte[] key, long exp) {
        if (System.currentTimeMillis() > exp) return Response.ReusedSignature.INSTANCE;

        final long readLock = this.lock.readLock();

        try {
            final var usedSignatures = this.usedSignatureMap.computeIfAbsent(getCurrentPeriod(), k -> new HashSet<>());
            final var wrappedSig = new WrappedByteArray(sig);
            if (usedSignatures.contains(wrappedSig)) return Response.ReusedSignature.INSTANCE;
            final var wrappedKey = new WrappedByteArray(key);
            final var loadedKey = this.hashToKey.get(wrappedKey);
            if (loadedKey == null) return Response.NoSuchKey.INSTANCE;

            if (!loadedKey.bc.verify(Ed25519.Algorithm.Ed25519, null, msg, 0, msg.length, sig, 0))
                return Response.InvalidSignature.INSTANCE;

            usedSignatures.add(wrappedSig);
            return new Response.SignatureValid(loadedKey);
        } finally {
            this.lock.unlockRead(readLock);
        }
    }

    public boolean doesNickRequireSignature(String nick) {
        return this.nicks.contains(nick.toLowerCase());
    }

    public byte[] createSignature(byte[] msg) {
        final byte[] signature = new byte[64];
        this.selfKey.sign(Ed25519.Algorithm.Ed25519, null, msg, 0, msg.length, signature, 0);
        return signature;
    }

    public sealed interface Response {
        final class NoSuchKey implements Response {
            public static final NoSuchKey INSTANCE = new NoSuchKey();

            private NoSuchKey() {

            }
        }

        final class InvalidSignature implements Response {
            public static final InvalidSignature INSTANCE = new InvalidSignature();

            private InvalidSignature() {

            }
        }

        final class ReusedSignature implements Response {
            public static final ReusedSignature INSTANCE = new ReusedSignature();

            private ReusedSignature() {

            }
        }

        record SignatureValid(LoadedKey key) implements Response {

        }
    }

    public static final class WrappedByteArray {
        private final byte[] arr;

        public WrappedByteArray(byte @Unmodifiable [] arr) {
            this.arr = arr;
        }

        @Override
        public String toString() {
            return HexFormat.of().formatHex(this.arr);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.arr);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof final WrappedByteArray wrappedByteArray && Arrays.equals(wrappedByteArray.arr, this.arr);
        }
    }


    public record LoadedKey(Ed25519PublicKeyParameters bc, Map<String, Set<String>> manifestUrlToClaims) {

    }
}
