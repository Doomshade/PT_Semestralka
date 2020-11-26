package git.doomshade.semstralka.smrha;

import git.doomshade.semstralka.Main;

/**
 * Abstraktní třída různých řešení pro tento problém
 *
 * @author Jakub Šmrha
 * @version 1.0 (27.10.2020)
 */
public abstract class Solution {

    /**
     * Matice cen
     */
    public final int[][] costMatrix;

    /**
     * Supply a demand
     */
    public final int[] supply, demand;

    /**
     * Zde uchováme data o tom, do jakého supermarketu jsme poslali kolik zboží
     */
    private final int[][] uplneJedno;

    /**
     * Data o total supply/demand a ceně
     */
    private int totalSupply, totalDemand, totalCost;


    /**
     * Značí, zda problém byl již už předtím vyřešen
     */
    private boolean solved = false;

    /**
     * Hlavní konstruktor
     *
     * @param costMatrix matice cen
     * @param supply     produkce
     * @param demand     poptávka
     */
    public Solution(int[][] costMatrix, int[] supply, int[] demand) {
        this.costMatrix = costMatrix;
        this.supply = supply;
        this.demand = demand;
        this.uplneJedno = new int[costMatrix.length][costMatrix[0].length];

        for (int s : supply) {
            totalSupply += s;
        }

        for (int s : demand) {
            totalDemand += s;
        }
    }

    /**
     * Pomocná enum pro vymazanou část matice
     */
    enum DeletedPart {
        ROW, COLUMN, BOTH
    }

    protected boolean shouldContinue() {
        return totalDemand > 0 && totalSupply > 0;
    }


    // mohli bychom vracet DeletedPart, ale pro mincost/vma je to useless

    /**
     * Přeškrtne řádek nebo sloupec v matici a přičte ho do total cost
     *
     * @param x x
     * @param y y
     * @return část, která se škrtla (0 = řádek, 1 = sloupec, 2 = řádek i sloupec)
     */
    protected DeletedPart choose(int x, int y) {

        // log
        Main.LOGGER.info(String.format("Choosing from index {x=%d, y=%d}%n", x, y));

        // cena cesty
        final int cost = costMatrix[y][x];

        // zjistime supply a demand pro danou bunku
        final int supply = this.supply[y];
        final int demand = this.demand[x];

        // logneme si totalSupply a demand pro zrychlení programu
        // program nemusí pokračovat, pokud je supply nebo demand nulový
        // TODO implementace ^
        totalSupply -= supply;
        totalDemand -= demand;

        // na základě tohoto indexu returnneme DeletedPart enum
        // 0 = row, 1 = column, 2 = both
        // index je spravne nastaven tak, ze dostaneme na konci cislo <0, 2>
        // tudiz nemusime ani checkovat pro outOfBounds
        // jedina vyjimka je negativni supply/demand, ale to je pak chyba v datech
        int chooseIndex = -1;

        // demand je vetsi nez supply
        // preskrtnout radek (x)
        if (demand >= supply) {
            deleteRow(x, y);
            chooseIndex++;
        }

        // supply je vetsi nez demand
        // preskrtnout sloupec (y)
        if (demand <= supply) {
            deleteColumn(x, y);
            chooseIndex += 2;
        }

        // log
        Main.LOGGER.info(String.format("<Computing value>\ncost: %d\nsupply: %d\ndemand: %d%n", cost, supply, demand));

        // value = cena cesty * pocet pouzitych supplies
        final int value = cost * Math.min(supply, demand);
        totalCost += value;
        uplneJedno[y][x] = Math.min(supply, demand);

        final DeletedPart deletedPart = DeletedPart.values()[chooseIndex];

        // log
        Main.LOGGER.info("DeletedPart " + deletedPart);
        Main.LOGGER.info("-----------------------------------------");
        return deletedPart;
    }

    /**
     * "Vymaže" řádek v matici (resp. sníží poptávku o produkci a produkci vynuluje)
     *
     * @param x x v cost matici
     * @param y y v cost matici
     */
    private void deleteRow(int x, int y) {

        // delete row <=> supply = 0
        // demand > supply -> snizime demand a smazeme supply
        demand[x] -= supply[y];
        supply[y] = 0;
    }

    /**
     * "Vymaže" sloupec v matici (resp. sníží produkci o poptávku a poptávku vynuluje)
     *
     * @param x x v cost matici
     * @param y y v cost matici
     */
    private void deleteColumn(int x, int y) {


        // delete column <=> demand = 0
        // supply > demand -> snizime supply a smazeme demand
        supply[y] -= demand[x];
        demand[x] = 0;
    }

    /**
     * Vyřeší problém a vrátí o něm data
     *
     * @return data
     * @throws RuntimeException pokud byl již problém vyřešen
     */
    public final SolutionData solve() {

        // nechceme, abychom řešili problém dvakrát (ani to nelze tak dělat, jelikož se pole změní během chodu solution)
        if (solved) {
            throw new RuntimeException("Problém byl již vyřešen");
        }
        solveProblem();
        solved = true;
        return new SolutionData(getClass(), totalCost, uplneJedno);
    }

    /**
     * Vyřeší problém
     */
    protected abstract void solveProblem();
}
