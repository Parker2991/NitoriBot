package land.chipmunk.parker2991.nitoribot.nightofnights.data.world;

import java.util.List;

public record BlockData (String block, int id, List<Integer> stateIds) {
    public void stateIds (List<Integer> stateIds) {
    }
}