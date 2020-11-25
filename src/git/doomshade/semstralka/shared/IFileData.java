package git.doomshade.semstralka.shared;

import git.doomshade.semstralka.util.Pair;

/**
 * @author Jakub Šmrha
 * @version 1.0 (22.11.2020)
 */
public interface IFileData {

    /**
     * Matice produkce továren do supermarketů během <b>t</b> dnů.<br>
     * Např. {@code [8][4][2][14] = 450} <=><br>
     * <b>9.</b> den produkce <b>5.</b> továrny do <b>3.</b> supermarketu <b>15. druhu zboží</b> bylo <b>450</b>.
     *
     * <ul>
     * <li>[t][x][y][z]</li>
     * <li>t = dny</li>
     * <li>x = továrny</li>
     * <li>y = supermarkety</li>
     * <li>z = typ zboží</li>
     * </ul>
     *
     * @return matici produkce továren do supermarketů během <b>t</b> dnů
     */
    int[][][][] getProduction();

    /**
     * Matice over-produkce továren během <b>t</b> dnů.<br>
     * Např. {@code [4][11][0] = 50} <=><br>
     * <b>5.</b> den over-produkce <b>12.</b> továrny <b>1.</b> druhu zboží bylo 50.
     *
     * <ul>
     * <li>[t][x][y]</li>
     * <li>t = dny</li>
     * <li>x = továrny</li>
     * <li>y = zboží</li>
     * </ul>
     *
     * @return matici over-produkce továren během <b>t</b> dnů.<br>
     */
    int[][][] getOverProduction();

    /**
     * Matice zásob supermarketů během <b>t</b> dnů.<br>
     * Např. {@code [2][7][4] = 100} <=><br>
     * <b>3.</b> den <b>8.</b> supermarketu <b>5.</b> druhu zboží byly zásoby na <b>100</b>
     *
     * <ul>
     * <li>[t][x][y]</li>
     * <li>t = dny</li>
     * <li>x = supermarkety</li>
     * <li>y = zboží</li>
     * </ul>
     *
     * @return matici zásob supermarketů během <b>t</b> dnů.<br>
     */
    int[][][] getSupermarketOverview();

    /**
     * @return délka simulace (v ms)
     */
    long getSimulationLength();


    /**
     * Spárování dat {@code <T, V>}, kdy {@code <T>} je den, kdy nebylo možné supermarkety uzásobit, a {@code <V>} je matice
     * supermarketů a zásob.<br>
     * Např. {@code [1][6] = 15} <=><br>
     * <b>2.</b> den <b>7.</b> druhů zboží chybělo <b>15</b> kusů
     * <ul>
     * <li>[t][x][y]</li>
     * <li>t = dny</li>
     * <li>x = supermarkety</li>
     * <li>y = zboží</li>
     * </ul>
     *
     * @return {@code null}, pokud má úloha řešení, jinak viz popisek
     */
    Pair<Integer, Integer[][]> noSolutionData();
}