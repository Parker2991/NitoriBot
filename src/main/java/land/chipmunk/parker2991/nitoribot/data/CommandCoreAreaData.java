package land.chipmunk.parker2991.nitoribot.data;
import org.cloudburstmc.math.vector.Vector3i;

public class CommandCoreAreaData {
  public Vector3i start;
  public Vector3i end;

  public CommandCoreAreaData (Vector3i start, Vector3i end) {
    this.start = start;
    this.end = end;
  }
}