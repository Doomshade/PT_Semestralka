package git.doomshade.semstralka.smrha;

import java.util.Arrays;

/**
 * Výstupní data z řešení
 *
 * @author Jakub Šmrha
 * @version 1.0 (28.10.2020)
 */
public class SolutionData {

    /**
     * Třída, která byla použita pro řešení problému
     */
    public final Class<? extends Solution> solutionClass;

    /**
     * Celková cena
     */
    public final int totalCost;

    /**
     * Uhm
     */
    public final int[][] uplneJedno;


    SolutionData(Class<? extends Solution> solutionClass, int totalCost, int[][] uplneJedno) {
        this.solutionClass = solutionClass;
        this.totalCost = totalCost;
        this.uplneJedno = uplneJedno;
    }

    @Override
    public String toString() {
        return "SolutionData{" +
                "solutionClass=" + solutionClass.getSimpleName() +
                ", totalCost=" + totalCost +
                ", uplneJedno=" + Arrays.deepToString(uplneJedno) +
                '}';
    }
}
