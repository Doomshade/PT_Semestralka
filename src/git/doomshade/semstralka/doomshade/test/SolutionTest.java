package git.doomshade.semstralka.doomshade.test;

import git.doomshade.semstralka.Main;
import git.doomshade.semstralka.doomshade.Solution;
import git.doomshade.semstralka.impl.graph.Storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SolutionTest {

    //private static final Collection<short[][]> matrixes = new HashSet<>();

    public static void main(String[] args) throws IOException {

        Storage read = Main.read(new File("C:\\Users\\Doomshade\\Desktop\\skull\\PT\\semestralka\\tests\\real_small.txt"));
        test(read);
    }

    private static void test(Storage read) throws IOException {
        long time = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis() - time);
        final int y = read.pocetSupermarketu;
        final int x = read.pocetTovaren;

        short[][] matrix = new short[y][x];
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                matrix[i][j] = read.getData().cenaPrevozu[i * y + j];
            }
        }


        for (short[] shorts : matrix) {
            System.out.println(Arrays.toString(shorts));
        }

        System.out.println();
        final short[] supply = Arrays.copyOf(read.getData().produkceTovaren, read.pocetTovaren);
        final short[] demand = Arrays.copyOf(read.getData().poptavkaZbozi, read.pocetSupermarketu);


        System.out.println(Arrays.toString(supply));
        System.out.println(Arrays.toString(demand));


        int[] cpy = new int[demand.length];

        for (int i = 0; i < demand.length; i++) {
            cpy[i] = demand[i];
        }

        System.out.println(Arrays.stream(cpy).sum());

        final Solution solution = new Solution(
                matrix,
                supply,
                demand);

        solution.northWest();

        System.out.println(solution.getTotalCost());
    }
}
