package git.doomshade.semstralka.smrha;

import java.util.*;

/**
 * @author Jakub Šmrha
 * @version 1.0 (27.10.2020)
 */
public class MinCostSolution extends Solution {

    // zde namapujeme cost na kolekci indexů
    // treemap, protože chceme mít jako první indexy nejnižší cost
    private final TreeMap<Integer, Collection<Integer>> SUPPLY_INDEX_MAP = new TreeMap<>();

    public MinCostSolution(int[][] matrix, int[] supply, int[] demand) {
        super(matrix, supply, demand);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                int num = matrix[i][j];
                final Collection<Integer> coll = SUPPLY_INDEX_MAP.getOrDefault(num, new HashSet<>());
                final int idx = i * matrix[0].length + j;
                coll.add(idx);
                SUPPLY_INDEX_MAP.put(num, coll);
            }
        }
    }

    @Override
    protected void solveProblem() {
        minCost();
    }

    private void minCost() {

        // pokud je total supply nebo total demand nulový, můžeme přestat pokračovat
        // if (!shouldContinue()) return;

        // vezmeme si první entry v mapě (je to sorted, tudíž to vezme nejnižší cost v matici)
        final Map.Entry<Integer, Collection<Integer>> entry = SUPPLY_INDEX_MAP.pollFirstEntry();

        // entry je prázdný, došli jsme pravědpodobně na konec (došly zásoby :( )
        if (entry == null) return;

        final Collection<Integer> coll = entry.getValue();
        final Iterator<Integer> it = coll.iterator();

        // pokud je iterátor for whatever reason prázdný, tak znovu voláme mincost
        // PS: na začátku metody pollujeme z mapy a pokud je entry null, program skončí -> nestane se infinite rekurze
        if (!it.hasNext()) {
            minCost();
            return;
        }

        // číslo, kterým budeme dělit pro získání indexu v matici
        final int len = costMatrix[0].length;

        // musíme zjistit index v matici
        int idx, x, y;
        while (true) {

            // vezmeme první index v matici s nejmenší cenou a smažeme ho z kolekce (již jsme ho vybrali)
            idx = it.next();
            it.remove();

            // zjistíme si x a y v matici
            x = idx % len;
            y = idx / len;

            // supply je na "sloupcové" straně, zatímco demand na "řádkové" straně
            int supply = this.supply[y];
            int demand = this.demand[x];

            // našli jsme index, který nebyl vynulovaný, pokračujeme dál v programu
            if (supply != 0 && demand != 0) {
                // pokud to nebyl poslední index, vrátíme kolekci zpět do mapy
                if (!coll.isEmpty()) {
                    SUPPLY_INDEX_MAP.put(entry.getKey(), coll);
                }
                break;
            }

            // došli jsme na konec iterace, začneme od začátku
            // PS: viz na začátku programu !it.hasNext()
            if (!it.hasNext()) {
                minCost();
                return;
            }
        }

        // zavoláme choose, který "smaže" buňku z matice (a s ní řádek/sloupec)
        choose(x, y);

        // našel se index, jedeme dál
        minCost();
    }
}
