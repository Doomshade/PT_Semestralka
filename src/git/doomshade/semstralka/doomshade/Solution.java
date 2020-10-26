package git.doomshade.semstralka.doomshade;

public class Solution {
    private final short[][] matrix;
    private final short[] supply, demand;

    public static final int deletionNumber = 0;

    private final short[][] usedSupplies;

    private int totalCost = 0;

    public Solution(short[][] matrix, short[] supply, short[] demand) {
        this.matrix = matrix;
        this.supply = supply;
        this.demand = demand;
        this.usedSupplies = new short[matrix.length][matrix[0].length];
    }

    public void northWest() {
        totalCost = 0;

        System.out.println();
        System.out.println("-----------------------------------------");
        northWest(0, 0);
    }

    public int getTotalCost() {
        return totalCost;
    }

    private void northWest(int x, int y) {
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
        final Pair<Integer, Chosen> pair = choose(x, y);
        totalCost += pair.t;
        System.out.println("Chosen " + pair);
        System.out.println("-----------------------------------------");

        switch (pair.v) {
            case ROW:
                northWest(x, y + 1);
                break;
            case COLUMN:
                northWest(x + 1, y);
                break;
            case BOTH:
                northWest(x + 1, y + 1);
                break;
        }
    }

    private static class Pair<T, V> {
        T t;
        V v;

        private Pair(T t, V v) {
            this.t = t;
            this.v = v;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "t=" + t +
                    ", v=" + v +
                    '}';
        }
    }

    enum Chosen {
        ROW, COLUMN, BOTH
    }


    private Pair<Integer, Chosen> choose(int x, int y) {

        // cena cesty
        final short cost = matrix[x][y];

        // zjistime supply a demand pro danou bunku
        final short supply = this.supply[y];
        final short demand = this.demand[x];

        // na základě tohoto indexu returnneme Chosen enum
        // 0 = row, 1 = column, 2 = both
        // index je spravne nastaven tak, ze dostaneme na konci cislo <0, 2>
        // tudiz nemusime ani checkovat pro outOfBounds
        // jedina vyjimka je negativni supply/demand, ale to je pak chyba v datech
        int chooseIndex = -1;

        // neni supply, musime se posunout o radek
        // zde nemazeme row ani column
        if (supply == 0) {
            chooseIndex += 2;
        }
        // demand je vetsi nez supply
        // preskrtnout radek (x)
        else if (demand >= supply) {
            deleteRow(x, y);
            chooseIndex++;
        }

        // neni demand, musime se posunout o sloupec
        if (demand == 0) {
            chooseIndex++;
        }
        // supply je vetsi nez demand
        // preskrtnout sloupec (y)
        else if (demand <= supply) {
            deleteColumn(x, y);
            chooseIndex += 2;
        }

        // z enum indexu ziskame chosen (row/column/both)
        final Chosen chosen = Chosen.values()[chooseIndex];

        // value = cena cesty * pocet pouzitych supplies
        System.out.printf("<Computing value>\ncost: %d\nsupply: %d\ndemand: %d%n", cost,supply,demand);
        final int value = cost * Math.min(supply, demand);
        return new Pair<>(value, chosen);
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
}
