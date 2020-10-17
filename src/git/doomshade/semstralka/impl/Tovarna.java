package git.doomshade.semstralka.impl;

public class Tovarna extends ValueHolder {
    public Tovarna(short value) {
        super(value);
    }

    @Override
    public String toString() {
        return "T " + super.toString();
    }
}
