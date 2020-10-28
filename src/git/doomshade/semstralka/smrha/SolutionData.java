package git.doomshade.semstralka.smrha;

import java.util.Arrays;

/**
 * @author Jakub Šmrha
 * @version 1.0 (28.10.2020)
 */
public class SolutionData {
    public final Class<? extends Solution> solutionClass;
    public final int totalCost;
    public final int[][] uplneJedno;


    public SolutionData(Class<? extends Solution> solutionClass, int totalCost, int[][] uplneJedno) {
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
