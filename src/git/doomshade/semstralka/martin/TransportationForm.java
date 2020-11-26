package git.doomshade.semstralka.martin;

import java.util.List;

/**
 * Reprezentuje tvar přepravního problému,
 * tak aby fungoval s potřebnými algoritmi potřebnou
 *
 * @author Martin Jakubašek
 */
public class TransportationForm {
    /** supermarkety */
    final List<Entity> supermarkets;
    /** továrny */
    final List<Entity> factories;
    /** matice cen */
    final int[][] costMatrix;

    /**
     * řešení
     */
    public double[][] solution;
    /** optimální řešení */
    public double[][] optimSolution;

    /**
     * Vytvoří novou instanci reprezentuijící 1 přepravní problém (tj. pro jeden druh zboží)
     *
     * @param supermarkets supermarkety
     * @param factories    továrny
     * @param costMatrix   matice ceny přepravy
     */
    public TransportationForm(List<Entity> supermarkets, List<Entity> factories, int[][] costMatrix) {
        this.supermarkets = supermarkets;
        this.factories = factories;
        this.costMatrix = costMatrix;
    }
}
