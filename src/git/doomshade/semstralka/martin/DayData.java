package git.doomshade.semstralka.martin;

import java.util.Arrays;

/**
 * Třída obsahuje informace z 1 dne po vykonání algoritmu.
 * Obsahuje -> matici ceny přepravy
 * -> matici prvotní přepravy každého zboží
 * -> matici optimální přepravy každého zboží
 * -> celkovou cenu přepravy prvotního a optimálního řešení
 *
 * @author Martin Jakubašek
 */
public class DayData {

    private final TransportationForm[] transportationForms;
    private final int factories;
    private final int supermarkets;

    /**
     * celková cena přepravy pro jeden den pro každé zboží
     */
    public final int[] feasiblePrice;
    /**
     * optimální cena přepravy pro jeden den pro každé zboží
     */
    public final int[] optimalPrice;
    /**
     * matice přepravy pro všehcny zboží
     */
    private final int[][][] arcMatrix;

    /**
     * Instance obsahuje data z 1 dne algoritmu
     *
     * @param transportationForms data z přepravních problému pro všechna zboží
     */
    public DayData(TransportationForm[] transportationForms, int supermarkets, int factories) {
        this.transportationForms = transportationForms;
        this.supermarkets = supermarkets;
        this.factories = factories;

        this.arcMatrix = createTransportationMatrices();

        this.feasiblePrice = getFeasiblePrice();
        this.optimalPrice = getOptimalPrice();
    }

    /**
     * vytvoří matici přepravy pro všechny druhy zboží ve trvaru [z,d,s]
     *
     * @return matice přepravy pro všechna zboží ve tvaru [z,d,s]
     */
    private int[][][] createTransportationMatrices() {
        int[][][] transportationMatrices = new int[transportationForms.length][factories][supermarkets];

        for (int z = 0; z < transportationMatrices.length; z++) {
            TransportationForm actual = transportationForms[z];
            int nextFactory = 0;
            for (int d = 0; d < transportationMatrices[0].length; d++) {
                int yPos = actual.factories.get(d).index;
                if (yPos != d)
                    continue;
                int nextSupermarket = 0;
                for (int s = 0; s < transportationMatrices[0][0].length; s++) {
                    int xPos = actual.supermarkets.get(s).index;
                    if (xPos != s)
                        continue;
                    transportationMatrices[z][d][s] = (int) actual.optimSolution[nextFactory][nextSupermarket];
                    nextSupermarket++;
                }
                nextFactory++;
            }
        }

        return transportationMatrices;
    }

    /**
     * Vrátí matice přepravy pro všechny druhy zboží ve tvaru [z,d,s]
     *
     * @return matice přepravy pro všechny druhy zboží ve tvaru [z,d,s]
     */
    public int[][][] getTransportationMatrices() {
        return Arrays.copyOf(arcMatrix, arcMatrix.length);
    }

    /**
     * Vrátí pole cen přepravy zboží v prvotním řešení
     *
     * @return pole cen přepravy zboží v prvotním řešení
     */
    private int[] getFeasiblePrice() {
        int[] feasiblePrice = new int[transportationForms.length];

        //TODO idk jestli je to třeba

        return feasiblePrice;
    }

    /**
     * Vrátí pole cen přepravy zboží v optimálním řešení
     *
     * @return pole cen přepravy zboží v optimálním řešení
     */
    private int[] getOptimalPrice() {
        int[] optimalPrice = new int[transportationForms.length];

        for (int i = 0; i < transportationForms.length; i++) {
            TransportationForm actual = transportationForms[i];
            for (int d = 0; d < actual.factories.size(); d++) {
                for (int s = 0; s < actual.supermarkets.size(); s++) {
                    optimalPrice[i] += actual.optimSolution[d][s] * actual.costMatrix[d][s];
                }
            }
        }

        return optimalPrice;
    }

}
