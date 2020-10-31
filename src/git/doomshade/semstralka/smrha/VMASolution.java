package git.doomshade.semstralka.smrha;

/**
 * @author Jakub Šmrha
 * @version 1.0 (31.10.2020)
 */
public class VMASolution extends Solution {
    /**
     * Hlavní konstruktor
     *
     * @param costMatrix matice cen
     * @param supply     produkce
     * @param demand     poptávka
     */
    public VMASolution(int[][] costMatrix, int[] supply, int[] demand) {
        super(costMatrix, supply, demand);
    }

    @Override
    protected void solveProblem() {

    }
}
