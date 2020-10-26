package git.doomshade.semstralka.martin;

import git.doomshade.semstralka.impl.graph.Storage;

import java.util.Arrays;

public class Simulation {

    private final Storage storage;

    public final int[][] costMatrix;

    public Simulation(Storage storage) {
        this.storage = storage;
        this.costMatrix = getCostMatrix();
    }

    private int[][] getCostMatrix() {
        int[][] costMatrix = new int[storage.pocetTovaren][storage.pocetSupermarketu];

        for (int y = 0; y < costMatrix.length; y++) {
            for (int x = 0; x < costMatrix[0].length; x++) {
                costMatrix[y][x] = storage.getCena(x,y);
            }
        }

        return costMatrix;
    }

    public void printCostMatrix() {
        for (int[] row : costMatrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    public void printCost1D() {
        System.out.println(Arrays.toString(storage.getData().cenaPrevozu));
    }
}
