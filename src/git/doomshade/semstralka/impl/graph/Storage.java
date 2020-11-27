package git.doomshade.semstralka.impl.graph;

import java.util.Arrays;

/**
 * Třída, ve které jsou data reprezentována v jednodimenzionálním poli. Data lze přečíst pomocí pomocných get metod.
 * Zde se uschovají veškerí data načtená ze souboru, která se dále dají jednoduše předělat do jiných struktur.
 *
 * @author Jakub Šmrha
 * @version 1.0
 */
public class Storage {


    /**
     * Pole dat
     */
    private final int[] cenaPrevozu, pocatecniZasoby, produkceTovaren, poptavkaZbozi;

    /**
     * Velikost pole dat
     */
    public final int pocetTovaren, pocetSupermarketu, pocetDruhuZbozi, pocetDni;

    /**
     * Hlavní konstruktor pro uschování pole dat
     *
     * @param cenaPrevozu       cena provozu
     * @param pocatecniZasoby   počáteční zásoby
     * @param produkceTovaren   produkce továren
     * @param poptavkaZbozi     poptávka zboží
     * @param pocetTovaren      počet továren
     * @param pocetSupermarketu počet supermarketů
     * @param pocetDruhuZbozi   počet druhů zboží
     * @param pocetDni          počet dní
     */
    public Storage(int[] cenaPrevozu, int[] pocatecniZasoby, int[] produkceTovaren, int[] poptavkaZbozi, int pocetTovaren, int pocetSupermarketu, int pocetDruhuZbozi, int pocetDni) {
        this.cenaPrevozu = cenaPrevozu;
        this.pocatecniZasoby = pocatecniZasoby;
        this.produkceTovaren = produkceTovaren;
        this.poptavkaZbozi = poptavkaZbozi;
        this.pocetTovaren = pocetTovaren;
        this.pocetSupermarketu = pocetSupermarketu;
        this.pocetDruhuZbozi = pocetDruhuZbozi;
        this.pocetDni = pocetDni;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Likely not needed methods (might be quality of life)
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param supermarket supermarket
     * @param tovarna     tovarna
     * @return cenu na zaklade supermarketu a tovarny
     * @throws IllegalArgumentException pokud supermarket nebo tovarna
     */
    public int getCena(int supermarket, int tovarna) throws IllegalArgumentException {
        /*validate("pocetSupermarketu", supermarket);
        validate("pocetTovaren", tovarna);*/
        return cenaPrevozu[pocetSupermarketu * tovarna + supermarket];
    }

    /**
     * @param druhZbozi   druh zbozi
     * @param supermarket supermarket
     * @return zasoby zbozi daneho supermarketu
     * @throws IllegalArgumentException pokud druh zbozi nebo supermarket neexistuje
     */
    public int getZasoby(int druhZbozi, int supermarket) throws IllegalArgumentException {
        /*validate("pocetDruhuZbozi", druhZbozi);
        validate("pocetSupermarketu", supermarket);*/
        //return pocatecniZasoby[pocetDruhuZbozi * druhZbozi + supermarket];
        // bruh... cmon :D u can do better
        return pocatecniZasoby[pocetSupermarketu * druhZbozi + supermarket];
    }

    /**
     * @param druhZbozi druh zbozi
     * @param tovarna   tovarna
     * @param den       den
     * @return produkci tovarny daneho zbozi v dany den
     * @throws IllegalArgumentException pokud druh zbozi, tovarna nebo den neexistuje
     */
    public int getProdukce(int druhZbozi, int tovarna, int den) throws IllegalArgumentException {
        /*validate("pocetDruhuZbozi", druhZbozi);
        validate("pocetTovaren", tovarna);
        validate("pocetDni", den);*/

        // hopefully this is correct
        //return produkceTovaren[pocetDruhuZbozi * druhZbozi * pocetDni + pocetTovaren * den + tovarna];
        return produkceTovaren[(pocetTovaren * den + tovarna) + (pocetTovaren * pocetDni * druhZbozi)];
    }

    /**
     * @param supermarket supermarket
     * @param den         den
     * @param druhZbozi   druh zbozi
     * @return poptavku daneho zbozi supermarketu v dany den
     * @throws IllegalArgumentException pokud supermarket, den nebo druh zbozi neexistuje
     */
    public int getPoptavka(int supermarket, int den, int druhZbozi) throws IllegalArgumentException {

        /*validate("pocetSupermarketu", supermarket);
        validate("pocetDni", den);
        validate("pocetDruhuZbozi", druhZbozi);*/
        // test;
        // test2
        //return poptavkaZbozi[0];
        return poptavkaZbozi[(pocetSupermarketu * den + supermarket) + (pocetSupermarketu * pocetDni * druhZbozi)];
    }

    /**
     * Tato trida zaridi nemennost pole - nechceme nijak nikdy editovat raw data (kdyby nas to nekdy napadlo).
     * Data jsou tedy accessible, ale pouze jejich kopie. Zatim not used
     *
     * @author Jakub Smrha
     */
    public class Data {

        public final int[] cenaPrevozu = Arrays.copyOf(Storage.this.cenaPrevozu, Storage.this.cenaPrevozu.length);
        public final int[] pocatecniZasoby = Arrays.copyOf(Storage.this.pocatecniZasoby, Storage.this.pocatecniZasoby.length);
        public final int[] produkceTovaren = Arrays.copyOf(Storage.this.produkceTovaren, Storage.this.produkceTovaren.length);
        public final int[] poptavkaZbozi = Arrays.copyOf(Storage.this.poptavkaZbozi, Storage.this.poptavkaZbozi.length);
    }


    /**
     * Pomocna get metoda (v jave zavolani konstruktoru inner tridy nevypada zrovna nejlepe :/)
     *
     * @return novou instanci Data
     */
    public Data getData() {
        return new Data();
    }
}
