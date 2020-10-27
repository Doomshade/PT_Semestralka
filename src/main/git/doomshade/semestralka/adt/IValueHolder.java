package main.git.doomshade.semestralka.adt;

import main.git.doomshade.semestralka.impl.Supermarket;
import main.git.doomshade.semestralka.impl.Tovarna;

/**
 * <p>>Rozhraní pro třídy, které drží určitou hodnotu ({@link Supermarket}, {@link Tovarna}
 * <p>Možná nebude třeba ani využít
 *
 * @deprecated nevyužije se
 */
@Deprecated(forRemoval = true)
public interface IValueHolder {

    /**
     * @return hodnotu, kterou daný objekt právě drží
     */
    short getValue();
}
