package main.git.doomshade.semestralka.adt;

/**
 * Ohodnocená hrana mezi dvěma vrcholy
 *
 * @deprecated Nevyužije se
 */
@Deprecated(forRemoval = true)
public class Edge {

    // TODO the start attribute likely not needed
    /**
     * Počátek hrany
     */
    public final short start;

    /**
     * Konec hrany
     */
    public final short end;

    /**
     * Hodnota hrany
     */
    public final short value;

    /**
     * Hlavní konstruktor hrany
     *
     * @param start počátek hrany
     * @param end   konec hrany
     * @param value hodnota hrany
     */
    public Edge(short start, short end, short value) {
        this.start = start;
        this.end = end;
        this.value = value;
    }

    /**
     * Pomocný konstruktor pro neohodnocenou hranu (nastaví hodnotu na 0)
     *
     * @param start počátek hrany
     * @param end   konec hrany
     */
    public Edge(short start, short end) {
        this(start, end, 0);
    }

    /**
     * Overload {@link Edge#Edge(short, short, short)}, přetypuje int na short (abychom nemuseli neustále přetypovávat)
     *
     * @param start počátek hrany
     * @param end   konec hrany
     * @param value ohodnocení hrany
     */
    public Edge(int start, int end, int value) {
        this((short) start, (short) end, (short) value);
    }

    /**
     * Overload {@link #Edge(short, short)}, přetypuje int na short (abychom nemuseli neustále přetypovávat)
     *
     * @param start počátek hrany
     * @param end   konec hrany
     */
    public Edge(int start, int end) {
        this((short) start, (short) end);
    }


    @Override
    public String toString() {
        return "Edge{" +
                "" + start +
                ", " + end +
                ", " + value +
                '}';
    }
}
