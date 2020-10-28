package test.git.doomshade.semestralka.smrha;

import git.doomshade.semstralka.Main;
import git.doomshade.semstralka.impl.graph.Storage;
import git.doomshade.semstralka.smrha.MinCostSolution;
import git.doomshade.semstralka.smrha.Solution;
import git.doomshade.semstralka.util.MatrixUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Testová třída pro {@link Solution}
 *
 * @author Jakub Šmrha
 * @version 1.0 (27.10.2020)
 */
public class SolutionTest {

    //private static final Collection<short[][]> matrixes = new HashSet<>();


    @Test
    public void testSolutions() throws IOException {
        long time = System.currentTimeMillis();

        final File[] files = new File("C:\\Users\\Doomshade\\Desktop\\skull\\PT\\semestralka\\tests").listFiles();
        for (File file : files) {
            Storage read = Main.read(file);

            System.out.println(file.getName());
            testSolution(read);
        }

        System.out.println(System.currentTimeMillis() - time);
    }

    private void testSolution(Storage read) {

        short[][] matrix = MatrixUtil.createMatrix(read.getData().cenaPrevozu, read.pocetSupermarketu);

        final short[] supply = Arrays.copyOf(read.getData().produkceTovaren, read.pocetTovaren);
        final short[] demand = Arrays.copyOf(read.getData().poptavkaZbozi, read.pocetSupermarketu);

        int totalSupply = 0;
        for (short s : supply) {
            totalSupply += s;
        }

        int totalDemand = 0;

        for (short i : demand) {
            totalDemand += i;
        }


        System.out.println("Total supply: " + totalSupply);
        System.out.println("Total demand: " + totalDemand);
        System.out.println(new MinCostSolution(matrix, supply, demand).SUPPLY_INDEX_MAP);
        System.out.println();
        /*testSolution(new NWSolution(
                matrix,
                supply,
                demand));*/
        testSolution(new MinCostSolution(
                matrix,
                supply,
                demand));
    }

    private void testSolution(Solution solution) {
        final int solve = solution.solve();

        int supplyAmount = 0;
        for (short i : solution.supply) {
            supplyAmount += i;
        }

        int demandAmount = 0;

        for (short i : solution.demand) {
            demandAmount += i;
        }

        System.out.println("Cost: " + solve);
        System.out.println();
        //assertTrue(supplyAmount == 0 || demandAmount == 0);
    }
}
