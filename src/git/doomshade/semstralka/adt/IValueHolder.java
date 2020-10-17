package git.doomshade.semstralka.adt;

/**
 * <p>>Rozhraní pro třídy, které drží určitou hodnotu ({@link git.doomshade.semstralka.impl.Supermarket}, {@link git.doomshade.semstralka.impl.Tovarna}
 * <p>Možná nebude třeba ani využít
 */
public interface IValueHolder {

    /**
     * @return hodnotu, kterou daný objekt právě drží
     */
    short getValue();
}
