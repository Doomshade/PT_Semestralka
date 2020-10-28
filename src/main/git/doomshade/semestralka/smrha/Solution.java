package main.git.doomshade.semestralka.smrha;

/**
 * Solution pro tento problém
 *
 * @author Jakub Šmrha
 * @version 1.0 (27.10.2020)
 */
public abstract class Solution {

    public final short[][] costMatrix;

    public final int[][] uplneJedno;

    public final short[] supply, demand;

    private int totalCost = 0;

    public Solution(short[][] costMatrix, short[] supply, short[] demand) {
        this.costMatrix = costMatrix;
        this.uplneJedno = new int[costMatrix.length][costMatrix[0].length];
        this.supply = supply;
        this.demand = demand;
    }

    enum DeletedPart {
        ROW, COLUMN, BOTH
    }


    /**
     * Přeškrtne řádek nebo sloupec v matici a přičte ho do total cost
     *
     * @param x x
     * @param y y
     * @return část, která se škrtla
     */
    protected DeletedPart choose(int x, int y) {

        // cena cesty
        final short cost = costMatrix[y][x];

        // zjistime supply a demand pro danou bunku
        final short supply = this.supply[y];
        final short demand = this.demand[x];

        // na základě tohoto indexu returnneme DeletedPart enum
        // 0 = row, 1 = column, 2 = both
        // index je spravne nastaven tak, ze dostaneme na konci cislo <0, 2>
        // tudiz nemusime ani checkovat pro outOfBounds
        // jedina vyjimka je negativni supply/demand, ale to je pak chyba v datech
        int chooseIndex = -1;

        // TODO: wrong, delete l8r (kdyby náhodou to bylo actually right :D )
        // neni supply, musime se posunout o radek
        // zde nemazeme row ani column
        /*if (supply == 0) {
            chooseIndex += 2;
        }*/
        // demand je vetsi nez supply
        // preskrtnout radek (x)
        if (demand >= supply) {
            deleteRow(x, y);
            chooseIndex++;
        }

        // TODO: wrong, delete l8r (kdyby náhodou to bylo actually right :D )
        // neni demand, musime se posunout o sloupec
        /*if (demand == 0) {
            chooseIndex++;
        }*/
        // supply je vetsi nez demand
        // preskrtnout sloupec (y)
        if (demand <= supply) {
            deleteColumn(x, y);
            chooseIndex += 2;
        }

        // value = cena cesty * pocet pouzitych supplies
        System.out.printf("<Computing value>\ncost: %d\nsupply: %d\ndemand: %d%n", cost, supply, demand);
        final int value = cost * Math.min(supply, demand);
        totalCost += value;
        uplneJedno[y][x] = value;
        return DeletedPart.values()[chooseIndex];
    }

    private void deleteRow(int x, int y) {

        // delete row <=> supply = 0
        // demand > supply -> snizime demand a smazeme supply
        demand[x] -= supply[y];
        supply[y] = 0;
    }

    private void deleteColumn(int x, int y) {


        // delete column <=> demand = 0
        // supply > demand -> snizime supply a smazeme demand
        supply[y] -= demand[x];
        demand[x] = 0;
    }

    public final int solve() {
        totalCost = 0;
        solveProblem();
        return totalCost;
    }

    protected abstract void solveProblem();
}
