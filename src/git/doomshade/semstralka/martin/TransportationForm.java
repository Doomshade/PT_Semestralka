package git.doomshade.semstralka.martin;

import java.util.ArrayList;

/**
 * Reprezentuje tvar přepravního problému,
 * tak aby fungoval s potřebnými algoritmi potřebnou
 *
 * @author Martin Jakubašek
 */
public class TransportationForm {
    final ArrayList<Entity> supermarkets;
    final ArrayList<Entity> factories;
    final int[][] costMatrix;

    /**
     * řešení
     */
    public int[][] solution;

    /**
     * Vytvoří novou instanci reprezentuijící 1 přepravní problém (tj. pro jeden druh zboží)
     *
     * @param supermarkets supermarkety
     * @param factories    továrny
     * @param costMatrix   matice ceny přepravy
     */
    public TransportationForm(ArrayList<Entity> supermarkets, ArrayList<Entity> factories, int[][] costMatrix) {
        this.supermarkets = supermarkets;
        this.factories = factories;
        this.costMatrix = costMatrix;
    }
}
