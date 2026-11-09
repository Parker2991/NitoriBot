package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import com.google.common.hash.Hashing;
import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundChangeGameModePacket;

import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ValidationModule {
    private NitoriBot bot;
    public String trustedHash;
    public String adminHash;
    public String ownerHash;

    private String generateHash (String key) {
        final long time = System.currentTimeMillis() / 2000;
        final String value = String.format(
            "%s%s",
            time,
            key
        );

        return Hashing.sha256()
            .hashString(value, StandardCharsets.UTF_8)
            .toString()
            .substring(0, 16);
    }

    public ValidationModule (NitoriBot bot) {
        this.bot = bot;

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run () {
                trustedHash = generateHash(bot.config.keys.trusted);
                adminHash = generateHash(bot.config.keys.admin);
                ownerHash = generateHash(bot.config.keys.owner);
            }
        }, 0, 100);
    }
}
