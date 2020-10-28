package git.doomshade.semestralka.impl;

/**
 * @deprecated Nevyužije se
 */
@Deprecated(forRemoval = true)
public class Supermarket extends ValueHolder {
    public Supermarket(short value) {
        super(value);
    }

    @Override
    public String toString() {
        return "S " + super.toString();
    }
}
