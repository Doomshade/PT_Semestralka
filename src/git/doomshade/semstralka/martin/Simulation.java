package git.doomshade.semstralka.martin;

import git.doomshade.semstralka.impl.graph.Storage;

/**
 * @author Martin Jakubašek
 */
public class Simulation {

    public final Storage storage;

    // upravená vstupní data

    /**
     * matice ceny přepravy c_[d,s]
     */
    public final int[][] costMatrix;
    /**
     * matice zásob supermarketů q_[z,s]
     */
    public final int[][] stocksMatrix;
    /**
     * matice produkcí továren p_[d,t,z]
     */
    public final int[][][] productionMatrix;
    /**
     * matice poptávek supermarketů r_[s,t,z]
     */
    public final int[][][] demandMatrix;

    // důležité informace ze vstupních dat

    /**
     * matice celkové produkce továren za všechny dny simulace delta_p_[d,z]
     */
    public final int[][] totalFactorySimulationProduction;
    /**
     * matice celkové poptávky supermarketů za všechny dny simulace delta_r_[s,z]
     */
    public final int[][] totalSupermarketSimulationDemand;

    /**
     * součet všech zásob
     */
    public final int totalStocks;
    /**
     * součet všech produkcí
     */
    public final int totalProduction;
    /**
     * součet všech poptávek
     */
    public final int totalDemand;

    // konstruktor

    /**
     * Vytvoří a vrátí novou instanci celé simulace přepravy ze vstupních dat
     *
     * @param storage vstupní data simulace přepravy
     */
    public Simulation(Storage storage) {
        // zpracuj vstupní data
        this.storage = storage;
        this.costMatrix = getCostMatrix();
        this.stocksMatrix = getStocksMatrix();
        this.productionMatrix = getProductionMatrix();
        this.demandMatrix = getDemandMatrix();
        // získej informace ze vstupních dat
        this.totalFactorySimulationProduction = getTFSProduction();
        this.totalSupermarketSimulationDemand = getTSSDemand();

        this.totalStocks = getTotalStocks();
        this.totalProduction = getTotalProduction();
        this.totalDemand = getTotalDemand();
    }

    // veřejné metody

    /**
     * Den ve kterém se nachází simulace
     */
    private int currentDay = 0;

    /**
     * Simuluj následující den simulace
     *
     * @return obsah simulovaného dne
     */
    public Day simulateNextDay() {
        Day day = new Day(this);
        day.simulateDay();

        currentDay++;
        return day;
    }

    // zpracování vstupních dat

    /**
     * Vytvoří a vrátí matici ceny přepravy (tvar c_[d,s])
     *
     * @return matice ceny přepravy ve tvaru c_[d,s]
     */
    private int[][] getCostMatrix() {
        int[][] costMatrix = new int[storage.pocetTovaren][storage.pocetSupermarketu];

        for (int y = 0; y < costMatrix.length; y++) {
            for (int x = 0; x < costMatrix[0].length; x++) {
                costMatrix[y][x] = storage.getCena(x, y);
            }
        }

        return costMatrix;
    }

    /**
     * Vytvoří a vrátí matici zásob supermarketů (tvar q_[z,s])
     *
     * @return matice zásob supermarketů ve tvaru q_[z,s]
     */
    private int[][] getStocksMatrix() {
        int[][] stocksMatrix = new int[storage.pocetDruhuZbozi][storage.pocetSupermarketu];

        for (int y = 0; y < storage.pocetDruhuZbozi; y++) {
            for (int x = 0; x < storage.pocetSupermarketu; x++) {
                stocksMatrix[y][x] = storage.getZasoby(y, x);
            }
        }

        return stocksMatrix;
    }

    /**
     * Vytvoří a vrátí matici produkcí továren (tvar p_[d,t,z])
     *
     * @return matice produkcé továren ve tvaru p_[d,t,z]
     */
    private int[][][] getProductionMatrix() {
        int[][][] productionMatrix = new int[storage.pocetTovaren][storage.pocetDni][storage.pocetDruhuZbozi];

        for (int d = 0; d < productionMatrix.length; d++) {
            for (int t = 0; t < productionMatrix[0].length; t++) {
                for (int z = 0; z < productionMatrix[0][0].length; z++) {
                    productionMatrix[d][t][z] = storage.getProdukce(z, d, t);
                }
            }
        }

        return productionMatrix;
    }

    /**
     * Vytvoří a vrátí matici poptável supermarketů (tvar r_[s,t,z])
     *
     * @return matice poptávky supermarketů ve tvaru r_[s,t,z]
     */
    private int[][][] getDemandMatrix() {
        int[][][] demandMatrix = new int[storage.pocetSupermarketu][storage.pocetDni][storage.pocetDruhuZbozi];

        for (int s = 0; s < demandMatrix.length; s++) {
            for (int t = 0; t < demandMatrix[0].length; t++) {
                for (int z = 0; z < productionMatrix[0][0].length; z++) {
                    demandMatrix[s][t][z] = storage.getPoptavka(s, t, z);
                }
            }
        }

        return demandMatrix;
    }

    // informace ze vstupních dat

    /**
     * Vytvoří a vrátí matici celkové produkce továren za celou dobu simulace
     *
     * @return matice celkové produkce továren ve tvaru delta_p_[d,z]
     */
    private int[][] getTFSProduction() {
        int[][] tfsp = new int[storage.pocetTovaren][storage.pocetDruhuZbozi];

        for (int d = 0; d < productionMatrix.length; d++) {
            for (int t = 0; t < productionMatrix[0].length; t++) {
                for (int z = 0; z < productionMatrix[0][0].length; z++) {
                    tfsp[d][z] += productionMatrix[d][t][z];
                }
            }
        }

        return tfsp;
    }

    /**
     * Vytvoří a vrátí matici celkové poptávky supermarketů za celou dobu simulace
     *
     * @return matice celkové poptávky supermarketů ve tvaru delta_r_[s,z]
     */
    private int[][] getTSSDemand() {
        int[][] tfsp = new int[storage.pocetSupermarketu][storage.pocetDruhuZbozi];

        for (int s = 0; s < demandMatrix.length; s++) {
            for (int t = 0; t < demandMatrix[0].length; t++) {
                for (int z = 0; z < demandMatrix[0][0].length; z++) {
                    tfsp[s][z] += demandMatrix[s][t][z];
                }
            }
        }

        return tfsp;
    }

    /**
     * Vrátí celkovou produkci všech produktů za celou dobu simulace
     *
     * @return celková produkce všech produktů za celou dobu simulace
     */
    private int getTotalProduction() {
        int deltaProduction = 0;

        for (int[] d : totalFactorySimulationProduction) {
            for (int productProduction : d) {
                deltaProduction += productProduction;
            }
        }

        return deltaProduction;
    }

    /**
     * Vrátí celkovou poptávku všech supermarketů za celou dobu simulace
     *
     * @return celková poptávka všech supermarketů za celou dobu simulace
     */
    private int getTotalDemand() {
        int deltaDemand = 0;

        for (int[] s : totalSupermarketSimulationDemand) {
            for (int productDemand : s) {
                deltaDemand += productDemand;
            }
        }

        return deltaDemand;
    }

    /**
     * Vrátí celkové zásoby všech supermarketů
     *
     * @return celkové zásoby všech supermarketů
     */
    private int getTotalStocks() {
        int deltaStocks = 0;

        for (int[] z : stocksMatrix) {
            for (int s : z) {
                deltaStocks += s;
            }
        }

        return deltaStocks;
    }

    // GET-SET

    public int getCurrentDay() {
        return currentDay;
    }
}
