package git.doomshade.semstralka.impl;

/**
 * @deprecated Nevyužije se
 */
@Deprecated(forRemoval = true)
public class Tovarna extends ValueHolder {
    public Tovarna(short value) {
        super(value);
    }

    @Override
    public String toString() {
        return "T " + super.toString();
    }
}
