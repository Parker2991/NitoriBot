package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;

import java.util.ArrayList;
import java.util.List;

public class ExtrasMessagingModule {
    private NitoriBot bot;
    public final List<String> channels = new ArrayList<>();

    public ExtrasMessagingModule (NitoriBot bot) {
        this.bot = bot;
    }
}
