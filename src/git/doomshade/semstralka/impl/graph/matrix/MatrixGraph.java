package git.doomshade.semstralka.impl.graph.matrix;

import git.doomshade.semstralka.impl.graph.Graph;

import java.io.PrintStream;

/**
 * Maticová implementace grafu
 */
public abstract class MatrixGraph extends Graph {

    protected short[][] matrix;
    protected final short initNumber;

    /**
     * <p>Detaily: {@link Graph#Graph(boolean)}
     * <p>Nainicializuje hodnoty matice na initNumber
     *
     * @param oriented   zda je graf orientovaný
     * @param initNumber prvotní hodnota matice
     */
    public MatrixGraph(boolean oriented, short initNumber) {
        super(oriented);
        this.initNumber = initNumber;
    }

    /**
     * <p>Detaily: {@link Graph#Graph(boolean)}
     * <p>Na základě orientace nainicializuje hodnoty matice (orientovaný = -1, neorientovaný = 0)
     *
     * @param oriented zda je graf orientovaný
     */
    public MatrixGraph(boolean oriented) {
        this(oriented, (short) (oriented ? -1 : 0));
    }

    @Override
    public void initializeGraph(int size) {
        matrix = new short[size][size];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = initNumber;
            }
        }
    }

    /**
     * Zavolá {@link MatrixGraph#print(PrintStream, boolean)} s {@code true}
     *
     * @param out kam vypsat graf
     */
    @Override
    public void print(PrintStream out) {
        print(out, true);
    }

    /**
     * Vypíše graf do print streamu, přičemž případně vynechá nainicializované hodnoty
     *
     * @param out                        kam vypsat graf
     * @param leaveOutInitializedNumbers zda vynechat nainicializované hodnoty
     */
    public void print(PrintStream out, boolean leaveOutInitializedNumbers) {
        for (int i = 0; i < matrix.length; i++) {
            final short[] p = this.matrix[i];
            out.print(i + ": [");

            boolean printed = false;
            for (int j = 0; j < p.length; j++) {
                short num = p[j];

                if (num != initNumber || !leaveOutInitializedNumbers) {
                    if (printed) out.print(", ");

                    out.print(String.format("{%d, %d}", j, num));
                    printed = true;
                }

            }
            out.println("]");
        }
    }
}
