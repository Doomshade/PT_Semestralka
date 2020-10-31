package git.doomshade.semstralka.martin;

import git.doomshade.semstralka.smrha.MinCostSolution;
import git.doomshade.semstralka.smrha.SolutionData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Day {

    /**
     * odkaz na instanci simulace (obsahuje potřebná data)
     */
    private final Simulation simulation;
    /**
     * simulovaný den
     */
    public final int simDay;
    /**
     * kopie zásob supermarketů
     */
    private int[][] stocksAfterCheck; // [z,s]
    /**
     * co který supermarket potřebuje
     */
    private int[][] currentDemand; // [s][z]
    /**
     * tvar matice pro přepravní problém pro každé zboží
     */
    private final TransportationForm[] tfs;

    /**
     * Vytvoří novou instanci reprezentující 1 konkrétní den přepravy,
     * tj. instance určí co kdo potřebuje
     *
     * @param simulation simulace přepravy
     */
    public Day(Simulation simulation) {
        this.simulation = simulation;
        this.simDay = simulation.getCurrentDay();
        this.tfs = new TransportationForm[simulation.storage.pocetDruhuZbozi];
    }

    /**
     * Provede simulaci 1 dne a následně vrátí vrátí informace o průběhu dne
     *
     * @return informace o průběhu dne
     */
    public DayData simulateDay() {
        calculateCurrentDemandAndStocks();
        // teď je potřeba vytvořit pro každé zboží správný tvar abychom mohli provádět algoritmy pro optimalizaci dopravy
        createTFs();
        // pro všechny TF zjisti 1 použitelné řešení (třeba min-cost algoritmem)
        runAlgo();

        // vrátíme informace o průběhu algoritmu
        return new DayData(tfs, simulation.storage.pocetSupermarketu, simulation.storage.pocetTovaren);
    }

    /**
     * Vypočítá co a kam bude potřeba odvézt + změní zásoby produktů, které by jinak byli převezeny
     * Pokud by se stalo že by nebylo potřeba nic převážed demand se bude rovnat -1
     */
    private void calculateCurrentDemandAndStocks() {
        currentDemand = new int[simulation.storage.pocetSupermarketu][simulation.storage.pocetDruhuZbozi];
        stocksAfterCheck = Arrays.copyOf(simulation.stocksMatrix, simulation.stocksMatrix.length);

        for (int s = 0; s < simulation.storage.pocetSupermarketu; s++) {
            for (int z = 0; z < simulation.storage.pocetDruhuZbozi; z++) {
                int demand = simulation.demandMatrix[s][simDay][z];
                // jestliže není žádná poptávka, tak nic nepřevážíme a zásoby neměníme
                if (demand == 0) {
                    currentDemand[s][z] = -1;
                    continue;
                }
                // jinak zjisti mi zásoby, odečti je a pokud ti vyjde záporné číslo, tak budeme posílat zboží,
                // v opačném případě odečti jen zboží ze zásob supermarketu
                int stocks = simulation.stocksMatrix[z][s];
                int result = stocks - demand;
                // zásoby nestačili
                if (result < 0) {
                    currentDemand[s][z] = result * -1; // bude tedy potřeba doplnění z továren
                    stocksAfterCheck[z][s] = 0;
                }
                // zásoby stačily
                else {
                    currentDemand[s][z] = -1;
                    stocksAfterCheck[z][s] = result;
                }
            }
        }
    }

    /**
     * Vytvoří všechny potřebné údaje pro vykonání algoritmů pro zjištění min-cost přepravys
     */
    private void createTFs() {
        for (int z = 0; z < simulation.storage.pocetDruhuZbozi; z++) {
            ArrayList<Entity> supermarkets = assignSupermarkets(z);
            ArrayList<Entity> factories = assignFactories(z);
            //int[][] costMatrix = new int[factories.size()][supermarkets.size()]; // [d,s] // přesun
            // vytvoř dummy row
            //dummy řešení
            int deltaDemand = 0;
            for (Entity entity : supermarkets) {
                deltaDemand += entity.value;
            }
            int deltaProduction = 0;
            for (Entity entity : factories) {
                deltaProduction += entity.value;
            }

            if (deltaDemand > deltaProduction) {
                int dummyRow = deltaDemand - deltaProduction;
                factories.add(new Entity(-1, dummyRow));
            } else if (deltaDemand < deltaProduction) {
                int dummyCol = deltaProduction - deltaDemand;
                supermarkets.add(new Entity(-1, dummyCol));
            }

            int[][] costMatrix = new int[factories.size()][supermarkets.size()]; // [d,s]

            //vytvoř TransportationForm
            // vytvoř matici cen
            for (int y = 0; y < costMatrix.length; y++) {
                int yPos = factories.get(y).index;
                for (int x = 0; x < costMatrix[0].length; x++) {
                    int xPos = supermarkets.get(x).index;
                    if (yPos == -1) {
                        costMatrix[y][x] = 0;
                        continue;
                    }
                    if (xPos == -1) {
                        costMatrix[y][x] = 0;
                        continue;
                    }
                    costMatrix[y][x] = simulation.costMatrix[yPos][xPos];
                }
            }

            tfs[z] = new TransportationForm(supermarkets, factories, costMatrix);
        }
    }

    private void runAlgo() {
        for (TransportationForm tf : tfs) {
            int[] supply = convertArrayListToArray(tf.factories);
            int[] demand = convertArrayListToArray(tf.supermarkets);

            // feasible solution

            MinCostSolution mcs = new MinCostSolution(tf.costMatrix, supply, demand);
            SolutionData data = mcs.solve();

            // optimal feasible solution

            double[][] res = convertToDouble(data.uplneJedno);
            tf.solution = Arrays.copyOf(res, res.length);

            MODI modi = new MODI(res, tf.costMatrix);
            boolean optimalSuccesful = modi.calculateMODI();
            if (!optimalSuccesful)
                tf.optimSolution = null;
            else
                tf.optimSolution = Arrays.copyOf(res, res.length);
        }
    }

    private ArrayList<Entity> assignSupermarkets(int z) {
        ArrayList<Entity> supermarkets = new ArrayList<>();
        for (int s = 0; s < simulation.storage.pocetSupermarketu; s++) {
            int demand = currentDemand[s][z];
            if (demand == -1)
                continue;
            Entity supermarket = new Entity(s, demand);
            supermarkets.add(supermarket);
        }
        return supermarkets;
    }

    private ArrayList<Entity> assignFactories(int z) {
        ArrayList<Entity> factories = new ArrayList<>();
        for (int d = 0; d < simulation.storage.pocetTovaren; d++) {
            int production = simulation.productionMatrix[d][simDay][z];
            if (!(production > 0))
                continue;
            Entity factory = new Entity(d, production);
            factories.add(factory);
        }
        return factories;
    }

    private int[] convertArrayListToArray(ArrayList<Entity> entities) {
        int[] res = new int[entities.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = entities.get(i).value;
        }
        return res;
    }

    private double[][] convertToDouble(int[][] matrix) {
        double[][] res = new double[matrix.length][matrix[0].length];
        for (int y = 0; y < res.length; y++ ) {
            for (int x = 0; x < res[0].length; x++) {
                res[y][x] = matrix[y][x];
            }
        }
        return res;
    }
}
