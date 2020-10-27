package main.git.doomshade.semestralka.smrha;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeMap;

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
        /*final BiPredicate<Number, Number> predicate = (x, y) -> MatrixUtil.LOWEST_INTEGER.test(x, y) && supply[x.intValue()] != 0;
        final short search = MatrixUtil.search(matrix, predicate, MatrixUtil.LOWEST_NUMBER);
        System.out.println("MCS: " + search);*/
    }

    private void minCost(int x, int y) {
        // musime se kouknout, zda je x a y in bounds
        // pokud neni, tak nechceme delat nic
        if (x >= matrix[0].length || y >= matrix.length) return;


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
        // z enum indexu ziskame deletedPart (row/column/both)

        DeletedPart deletedPart = choose(x, y);
        System.out.println("DeletedPart " + deletedPart);
        System.out.println("-----------------------------------------");

        switch (deletedPart) {
            case ROW -> minCost(x, y + 1);
            case COLUMN -> minCost(x + 1, y);
            case BOTH -> minCost(x + 1, y + 1);
        }
    }
}
