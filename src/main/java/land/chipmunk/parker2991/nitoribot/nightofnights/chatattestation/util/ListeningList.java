package land.chipmunk.parker2991.nitoribot.nightofnights.chatattestation.util;

import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

public final class ListeningList<T> implements List<T> {
  private final Consumer<T> listener;
  private final List<T> handle;

  public ListeningList(Consumer<T> listener, List<T> handle) {
    this.listener = listener;
    this.handle = handle;
  }

  @Override
  public int size() {
    return this.handle.size();
  }

  @Override
  public boolean isEmpty() {
    return this.handle.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return this.handle.contains(o);
  }

  @Override
  public @NonNull Iterator<T> iterator() {
    return this.handle.iterator();
  }

  @Override
  public @NonNull Object @NonNull [] toArray() {
    return this.handle.toArray();
  }

  @Override
  public @NonNull <T1> T1 @NonNull [] toArray(@NonNull T1 @NonNull [] a) {
    return this.handle.toArray(a);
  }

  @Override
  public boolean add(T t) {
    this.listener.accept(t);
    return this.handle.add(t);
  }

  @Override
  public boolean remove(Object o) {
    return this.handle.remove(o);
  }

  @Override
  public boolean containsAll(@NonNull Collection<?> c) {
    //noinspection SlowListContainsAll
    return this.handle.containsAll(c);
  }

  @Override
  public boolean addAll(@NonNull Collection<? extends T> c) {
    c.forEach(this.listener);
    return this.handle.addAll(c);
  }

  @Override
  public boolean addAll(int index, @NonNull Collection<? extends T> c) {
    c.forEach(this.listener);
    return this.handle.addAll(index, c);
  }

  @Override
  public boolean removeAll(@NonNull Collection<?> c) {
    return this.handle.removeAll(c);
  }

  @Override
  public boolean retainAll(@NonNull Collection<?> c) {
    return this.handle.retainAll(c);
  }

  @Override
  public void clear() {
    this.handle.clear();
  }

  @Override
  public T get(int index) {
    return this.handle.get(index);
  }

  @Override
  public T set(int index, T element) {
    this.listener.accept(element);
    return this.handle.set(index, element);
  }

  @Override
  public void add(int index, T element) {
    this.listener.accept(element);
    this.handle.add(index, element);
  }

  @Override
  public T remove(int index) {
    return this.handle.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return this.handle.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return this.handle.lastIndexOf(o);
  }

  @Override
  public @NonNull ListIterator<T> listIterator() {
    return this.handle.listIterator();
  }

  @Override
  public @NonNull ListIterator<T> listIterator(int index) {
    return this.handle.listIterator(index);
  }

  @Override
  public @NonNull List<T> subList(int fromIndex, int toIndex) {
    return this.handle.subList(fromIndex, toIndex);
  }
}
