package land.chipmunk.parker2991.nitoribot.listeners;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListenerManager {
  public final List<Listener> listeners = new CopyOnWriteArrayList<>();

  public void addListener (Listener listener) {
    listeners.add(listener);
  }

  public void removeListener (Listener listener) {
    listeners.remove(listener);
  }

  public void clearListener () {
    listeners.clear();
  }
}