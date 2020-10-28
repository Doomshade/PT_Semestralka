package test.git.doomshade.semestralka.smrha;

import git.doomshade.semstralka.Main;
import git.doomshade.semstralka.impl.graph.Storage;
import git.doomshade.semstralka.smrha.MinCostSolution;
import git.doomshade.semstralka.smrha.NWSolution;
import git.doomshade.semstralka.smrha.Solution;
import git.doomshade.semstralka.smrha.SolutionData;
import git.doomshade.semstralka.util.MatrixUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;

/**
 * Testová třída pro {@link Solution}
 *
 * @author Jakub Šmrha
 * @version 1.0 (27.10.2020)
 */
public class SolutionTest {

    //private static final Collection<short[][]> matrixes = new HashSet<>();

    private static final Collection<Storage> STORAGES = new LinkedList<>();


    @BeforeClass
    public static void prepareLogger() {
        Main.LOGGER.setLevel(Level.SEVERE);
    }

    @Test
    public void prepareSolutions() throws IOException {
        final File[] files = new File("C:\\Users\\Doomshade\\Desktop\\skull\\PT\\semestralka\\tests").listFiles();
        for (File file : files) {
            Storage read = Main.read(file);
            STORAGES.add(read);
        }
    }

    public void testSolutions() {
        for (Storage storage : STORAGES)
            testSolution(storage);
    }

    private void testSolution(Storage read) {

        int[][] matrix = MatrixUtil.createMatrix(read.getData().cenaPrevozu, read.pocetSupermarketu);

        final int[] supply = Arrays.copyOf(read.getData().produkceTovaren, read.pocetTovaren);
        final int[] demand = Arrays.copyOf(read.getData().poptavkaZbozi, read.pocetSupermarketu);

        int totalSupply = 0;
        for (int s : supply) {
            totalSupply += s;
        }

        int totalDemand = 0;

        for (int i : demand) {
            totalDemand += i;
        }


        Main.LOGGER.info("Total supply: " + totalSupply);
        Main.LOGGER.info("Total demand: " + totalDemand);
        Main.LOGGER.info("");


        Main.LOGGER.info("Testing MinCost Solution");
        testSolution(new MinCostSolution(
                matrix,
                Arrays.copyOf(supply, supply.length),
                Arrays.copyOf(demand, demand.length)));
        Main.LOGGER.info("Testing NorthWest Solution");
        testSolution(new NWSolution(
                matrix,
                Arrays.copyOf(supply, supply.length),
                Arrays.copyOf(demand, demand.length)));


    }

    private void testSolution(Solution solution) {
        final SolutionData solve = solution.solve();

        int supplyAmount = 0;
        for (int i : solution.supply) {
            supplyAmount += i;
        }

        int demandAmount = 0;

        for (int i : solution.demand) {
            demandAmount += i;
        }

        Main.LOGGER.info("Cost: " + solve);
        Main.LOGGER.info("");
        //assertTrue(supplyAmount == 0 || demandAmount == 0);
    }
}
