package git.doomshade.semstralka.smrha;

import java.util.*;

/**
 * @author Jakub Šmrha
 * @version 1.0 (27.10.2020)
 */
public class MinCostSolution extends Solution {

    public final TreeMap<Short, Collection<Integer>> SUPPLY_INDEX_MAP = new TreeMap<>();

    public MinCostSolution(short[][] matrix, short[] supply, short[] demand) {
        super(matrix, supply, demand);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                short num = matrix[i][j];
                final Collection<Integer> coll = SUPPLY_INDEX_MAP.getOrDefault(num, new HashSet<>());
                coll.add(i * matrix.length + j);
                SUPPLY_INDEX_MAP.put(num, coll);
            }
        }
    }

    @Override
    protected void solveProblem() {
        minCost();
        /*final BiPredicate<Number, Number> predicate = (x, y) -> MatrixUtil.LOWEST_INTEGER.test(x, y) && supply[x.intValue()] != 0;
        final short search = MatrixUtil.search(matrix, predicate, MatrixUtil.LOWEST_NUMBER);
        System.out.println("MCS: " + search);*/
    }

    private void minCost() {
        // musime se kouknout, zda je x a y in bounds
        // pokud neni, tak nechceme delat nic

        // vezmeme si první entry v mapě (je to sorted)
        final Map.Entry<Short, Collection<Integer>> entry = SUPPLY_INDEX_MAP.pollFirstEntry();

        // entry je prázdný, došli jsme pravědpodobně na konec
        if (entry == null) return;

        final Collection<Integer> coll = entry.getValue();
        final Iterator<Integer> it = coll.iterator();

        // pokud je iterátor for whatever reason prázdný, tak znovu voláme mincost
        if (!it.hasNext()) {
            minCost();
            return;
        }

        final int len = costMatrix[0].length;
        int idx, x, y;
        while (true) {
            idx = it.next();
            x = idx % len;
            y = idx / len;
            it.remove();

            int supply = this.supply[y];
            int demand = this.demand[x];
            if (supply != 0 && demand != 0) {
                break;
            }

            // došli jsme na konec iterace, začneme od začátku
            if (!it.hasNext()) {
                minCost();
                return;
            }
        }

        // index jsme našli, pokud to nebyl poslední index, vrátíme zpět do mapy
        if (!coll.isEmpty()) {
            SUPPLY_INDEX_MAP.put(entry.getKey(), coll);
        }


        /*
        if (!xBounds) {
            northWest(x - 1, y + 1);
            return;
        }

        if (!yBounds) {
            northWest(x + 1, y - 1);
            return;
        }*/

        System.out.printf("Choosing from index {x=%d, y=%d}%n", x, y);
        DeletedPart deletedPart = choose(x, y);
        System.out.println("DeletedPart " + deletedPart);
        System.out.println("-----------------------------------------");

        // našel se index, jedeme dál
        minCost();
    }
}
