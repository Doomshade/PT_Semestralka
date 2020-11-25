package git.doomshade.semstralka.smrha;

import git.doomshade.semstralka.Main;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Jakub Šmrha
 * @version 1.0 (31.10.2020)
 */
public class VAMSolution extends Solution {

    private final int[] rowCost;
    private final int[] minRowCost;
    private final int[] secondMinRowCost;
    private final Collection<Integer> deletedIndexes = new TempCollectionClass<>();

    private static class TempCollectionClass<E> extends HashSet<E> {
        @Override
        public boolean add(E e) {
            final boolean b = super.add(e);
            Main.LOGGER.info("Added " + e + " to deleted indexes.");
            return b;
        }
    }

    private final int[] columnCost;
    private final int[] minColumnCost;
    private final int[] secondMinColumnCost;

    /**
     * Hlavní konstruktor
     *
     * @param costMatrix matice cen
     * @param supply     produkce
     * @param demand     poptávka
     */
    public VAMSolution(int[][] costMatrix, int[] supply, int[] demand) {
        super(costMatrix, supply, demand);
        // rowCost
        this.rowCost = new int[costMatrix.length];
        this.minRowCost = new int[costMatrix.length];
        this.secondMinRowCost = new int[costMatrix.length];
        this.columnCost = new int[costMatrix[0].length];
        this.minColumnCost = new int[costMatrix[0].length];
        this.secondMinColumnCost = new int[costMatrix[0].length];
        Main.LOGGER.info(Arrays.deepToString(costMatrix));
        Main.LOGGER.info(Arrays.toString(supply));
        Main.LOGGER.info(Arrays.toString(demand));
    }

    @Override
    protected void solveProblem() {
        VAM();
    }

    private void VAM() {
        // prvně resetneme costy
        // cost necháme na 0, mincost na max value
        Arrays.fill(rowCost, 0);
        Arrays.fill(minRowCost, Integer.MAX_VALUE);
        Arrays.fill(secondMinRowCost, Integer.MAX_VALUE);

        Arrays.fill(columnCost, 0);
        Arrays.fill(minColumnCost, Integer.MAX_VALUE);
        Arrays.fill(secondMinColumnCost, Integer.MAX_VALUE);

        // pak spočteme differences

        // column
        final boolean modified = calcColumnCost();

        // všechny sloupce byly přeškrtány <=> všechny řádky byly přeškrtány, program končí
        if (!modified) {
            return;
        }

        // row
        calcRowCost();

        // maxCost je třeba pro porovnání pouze
        int maxCost = Integer.MIN_VALUE;

        // zde značíme, zda jsme našli max cost v sloupci nebo řádku
        boolean row = true;

        // a toto je index buď řádku nebo sloupce (záleží viz výše ^)
        int maxCostIndex = -1;

        // prvně tedy začneme v řádku, pak sloupec (kvůli booleanu)
        for (int i = 0; i < rowCost.length; i++) {
            final int cost = rowCost[i];
            if (cost > maxCost) {
                maxCost = cost;
                maxCostIndex = i;
            }
        }

        for (int i = 0; i < columnCost.length; i++) {
            final int cost = columnCost[i];
            if (cost > maxCost) {
                // max cost se našel v sloupci, dáme row = false
                row = false;
                maxCost = cost;
                maxCostIndex = i;
            }
        }

        // nyní musíme najít nejmenší číslo v řádku/sloupci a jeho index pro cache toho, co jsme za řádek/sloupec smazali (abychom ho skipovali)
        int leastCost = Integer.MAX_VALUE;
        int leastCostIndex = -1;
        if (row) {
            for (int i = 0; i < costMatrix[0].length; i++) {
                final int cost = this.costMatrix[maxCostIndex][i];
                if (cost < leastCost) {
                    leastCost = cost;
                    leastCostIndex = i;
                }
            }
            addDeleted(choose(leastCostIndex, maxCostIndex), leastCostIndex, maxCostIndex);
        } else {
            for (int i = 0; i < costMatrix.length; i++) {
                final int cost = this.costMatrix[i][maxCostIndex];
                if (cost < leastCost) {
                    leastCost = cost;
                    leastCostIndex = i;
                }
            }
            // uložíme si, co jsme smazali
            // x a y
            addDeleted(choose(maxCostIndex, leastCostIndex), maxCostIndex, leastCostIndex);
        }

        // zavoláme opět program
        VAM();
    }

    private void addDeleted(DeletedPart deletedPart, int x, int y) {
        switch (deletedPart) {

            case ROW -> {
                // posuneme x na začátek pole
                x = x - (x % costMatrix[0].length);
                for (int i = 0; i < costMatrix[0].length; i++) {
                    deletedIndexes.add(mapToMatrix(x + i, y));
                }
            }
            case COLUMN -> {
                for (int i = 0; i < costMatrix.length; i++) {
                    deletedIndexes.add(mapToMatrix(x, i));
                }
            }
            case BOTH -> {

                // trocha kopírování :/ not proud of this, ale jsem už línej :D
                x = x - (x % costMatrix[0].length);
                for (int i = 0; i < costMatrix[0].length; i++) {
                    deletedIndexes.add(mapToMatrix(x + i, y));
                }
                for (int i = 0; i < costMatrix.length; i++) {
                    deletedIndexes.add(mapToMatrix(x, i));
                }
            }
        }
    }


    private int mapToMatrix(int x, int y) {
        return y * costMatrix[0].length + x;
    }

    private void calcRowCost() {
        for (int i = 0; i < costMatrix.length; i++) {
            for (int j = 0; j < costMatrix[0].length; j++) {
                if (deletedIndexes.contains(mapToMatrix(j, i))) {
                    continue;
                }
                final int cost = this.costMatrix[i][j];
                if (cost < minRowCost[i]) {
                    secondMinRowCost[i] = minRowCost[i];
                    minRowCost[i] = cost;
                } else if (cost < secondMinRowCost[i]) {
                    secondMinRowCost[i] = cost;
                }
            }
        }

        for (int i = 0; i < rowCost.length; i++) {
            rowCost[i] = secondMinRowCost[i] - minRowCost[i];
        }
    }

    private boolean calcColumnCost() {
        boolean modified = false;
        for (int i = 0; i < costMatrix[0].length; i++) {
            for (int j = 0; j < costMatrix.length; j++) {
                if (deletedIndexes.contains(mapToMatrix(i, j))) {
                    continue;
                }
                modified = true;
                final int cost = this.costMatrix[j][i];
                if (cost < minColumnCost[i]) {
                    secondMinColumnCost[i] = minColumnCost[i];
                    minColumnCost[i] = cost;
                } else if (cost < secondMinColumnCost[i]) {
                    secondMinColumnCost[i] = cost;
                }
            }
        }
        if (!modified) {
            return false;
        }

        for (int i = 0; i < columnCost.length; i++) {
            columnCost[i] = secondMinColumnCost[i] - minColumnCost[i];
        }
        return true;
    }
}
