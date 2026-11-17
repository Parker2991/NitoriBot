package land.chipmunk.parker2991.nitoribot.nightofnights.command;

public enum CommandTrustLevels {
    PUBLIC(0),
    TRUSTED(1),
    ADMIN(2),
    OWNER(3),
    CONSOLE(4),
    DISABLED(-1);

    private final int level;

    private CommandTrustLevels (int level) {
        this.level = level;
    }

    public int level () {
        return level;
    }
}

/*
private final int level;

    private Level(int level) {
        this.level = level;
    }

    public int level() {
        return level;
    }
 */