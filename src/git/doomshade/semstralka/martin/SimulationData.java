package git.doomshade.semstralka.martin;

import git.doomshade.semstralka.shared.IFileData;
import git.doomshade.semstralka.util.Pair;

import java.util.Arrays;

public class SimulationData implements IFileData {

    private final Simulation simulation;

    private final long simLength;
    private final int[][][][] production;
    private final int[][][] supermarketOverview;
    private final int[][][] overProduction;

    public SimulationData(Simulation simulation) {

        this.simulation = simulation;

        //-time
        simLength = Arrays.stream(simulation.timeBenchmark).sum();
        //-
        production = createProduction();
        supermarketOverview = createSupermarketOverview();
        overProduction = createOverProduction();
    }

    private int[][][][] createProduction() {

        int[][][][] production = new int[simulation.daysData.length][simulation.storage.pocetTovaren][simulation.storage.pocetSupermarketu][simulation.storage.pocetDruhuZbozi];

        for (int day = 0; day < simulation.daysData.length; day++) {
            DayData currDay = simulation.daysData[day];
            if (currDay != null) {
                int[][][] transports = currDay.getTransportationMatrices(); // [z][d][s]
                for (int z = 0; z < transports.length; z++) {
                    for (int d = 0; d < transports[0].length; d++) {
                        for (int s = 0; s < transports[0][0].length; s++) {
                            // chcu [d][s][z]
                            production[day][d][s][z] = transports[z][d][s];
                        }
                    }
                }
            }
        }

        return production;
    }

    private int[][][] createSupermarketOverview() {
        int[][][] supOverview = new int[simulation.daysData.length][simulation.storage.pocetSupermarketu][simulation.storage.pocetDruhuZbozi];

        for (int day = 0; day < simulation.daysData.length; day++) {
            DayData currDay = simulation.daysData[day];
            if (currDay != null) {
                int[][] stocks = currDay.getStocks(); // [z][s]
                for (int z = 0; z < stocks.length; z++) {
                    for (int s = 0; s < stocks[0].length; s++) {
                        supOverview[day][s][z] = stocks[z][s];
                    }
                }
            }
        }

        return supOverview;
    }

    private int[][][] createOverProduction() {
        int[][][] overProd = new int[simulation.daysData.length][simulation.storage.pocetTovaren][simulation.storage.pocetDruhuZbozi];

        for (int day = 0; day < simulation.daysData.length; day++) {
            DayData currDay = simulation.daysData[day];
            if (currDay != null) {
                int[][][] transports = currDay.getTransportationMatrices(); // [z][d][s]
                for (int z = 0; z < transports.length; z++) {
                    for (int d = 0; d < transports[0].length; d++) {

                        int totProd = simulation.storage.getProdukce(z, d, day);

                        for (int s = 0; s < transports[0][0].length; s++) {
                            // chcu [d][z]
                            totProd -= transports[z][d][s];
                        }

                        overProd[day][d][z] = totProd;
                    }
                }
            }
        }

        return overProd;
    }

    @Override
    public int[][][][] getProduction() {
        return production;
    }

    @Override
    public int[][][] getOverProduction() {
        return overProduction;
    }

    @Override
    public int[][][] getSupermarketOverview() {
        return supermarketOverview;
    }

    @Override
    public long getSimulationLength() {
        return simLength;
    }

    @Override
    public Pair<Integer, Integer[][]> noSolutionData() {
        return null;
    }
}
