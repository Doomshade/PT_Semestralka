package git.doomshade.semestralka.util;

/**
 * Třída reprezentující pár
 *
 * @param <T>
 * @param <V>
 * @author Jakub Šmrha
 * @version 1.0
 */
public class Pair<T, V> {
    public T t;
    public V v;

    public Pair(T t, V v) {
        this.t = t;
        this.v = v;
    }
}
