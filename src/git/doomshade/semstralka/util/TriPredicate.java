package git.doomshade.semstralka.util;

import java.util.function.BiPredicate;

/**
 * @author Jakub Šmrha
 * @version 1.0 (27.10.2020)
 */
public interface TriPredicate<T, U, V> extends BiPredicate<T, U> {

    @Override
    default boolean test(T t, U u) {
        return test(t, u, null);
    }

    boolean test(T t, U u, V v);
}
