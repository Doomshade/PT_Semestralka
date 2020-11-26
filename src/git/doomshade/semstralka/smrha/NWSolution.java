package git.doomshade.semstralka.smrha;

/**
 * Řešení north-west
 *
 * @author Jakub Šmrha
 * @version 1.0 (27.10.2020)
 */
public class NWSolution extends Solution {

    /**
     * Hlavní konstruktor
     *
     * @param matrix matice cen
     * @param supply produkce
     * @param demand poptávka
     */
    public NWSolution(int[][] matrix, int[] supply, int[] demand) {
        super(matrix, supply, demand);
    }

    @Override
    protected void solveProblem() {
        northWest(0, 0);
    }

    /**
     * Hlavní metoda třídy
     *
     * @param x x
     * @param y y
     */
    private void northWest(int x, int y) {
        // musime se kouknout, zda je x a y in bounds
        // pokud neni, tak nechceme delat nic
        // také, pokud je total supply nebo demand nulový, nemá cenu pokračovat
        // if (!shouldContinue()) return;
        if (x >= costMatrix[0].length || y >= costMatrix.length) return;

        //MatrixUtil.searchIndex(costMatrix, MatrixUtil.HIGHEST_INTEGER, MatrixUtil.LOWEST_NUMBER);

        // choose nám vrátí část, která byla smazána
        // na jejím základě pokračujeme v programu
        switch (choose(x, y)) {
            case ROW:
                northWest(x, y + 1);
                break;
            case COLUMN:
                northWest(x + 1, y);
                break;
            case BOTH:
                northWest(x + 1, y + 1);
                break;
            default:
                throw new RuntimeException("Failed to choose");
        }
    }
}
