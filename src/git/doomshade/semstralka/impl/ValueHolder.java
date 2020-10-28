package git.doomshade.semstralka.impl;

import git.doomshade.semstralka.adt.IValueHolder;

/**
 * <p>>Implementace pro třídy, které drží určitou hodnotu ({@link Supermarket}, {@link Tovarna}
 * <p>Možná nebude třeba ani využít
 * @deprecated určo se nevyužije
 */
@Deprecated(forRemoval = true)
abstract class ValueHolder implements IValueHolder {
    private final short value;

    public ValueHolder(short value) {
        this.value = value;
    }

    @Override
    public final short getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "";
    }
}
