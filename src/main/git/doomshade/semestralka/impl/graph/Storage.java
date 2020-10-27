package main.git.doomshade.semestralka.impl.graph;

import java.lang.reflect.Field;
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
     * Pole dat, postačí short
     */
    private final short[] cenaPrevozu, pocatecniZasoby, produkceTovaren, poptavkaZbozi;

    /**
     * Velikost pole dat, postačí short
     */
    public final short pocetTovaren, pocetSupermarketu, pocetDruhuZbozi, pocetDni;

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
    public Storage(short[] cenaPrevozu, short[] pocatecniZasoby, short[] produkceTovaren, short[] poptavkaZbozi, short pocetTovaren, short pocetSupermarketu, short pocetDruhuZbozi, short pocetDni) {
        this.cenaPrevozu = cenaPrevozu;
        this.pocatecniZasoby = pocatecniZasoby;
        this.produkceTovaren = produkceTovaren;
        this.poptavkaZbozi = poptavkaZbozi;
        this.pocetTovaren = pocetTovaren;
        this.pocetSupermarketu = pocetSupermarketu;
        this.pocetDruhuZbozi = pocetDruhuZbozi;
        this.pocetDni = pocetDni;
    }

    /**
     * Validuje input (není pravděpodobně nutný, ale just to make sure)
     *
     * @param fieldName s jakym variablem to porovnat (pointery pls :( ) (dny, druhy zbozi, produkce, poptavka)
     * @param num       číslo, které má porovnat
     * @throws IllegalArgumentException pokud je číslo větší než počet (dnů, druhu zbozi, ...)
     */
    private void validate(String fieldName, Number num) throws IllegalArgumentException {
        short fieldValue;
        try {
            final Field declaredField = getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            fieldValue = declaredField.getShort(this);
        } catch (Exception e) {
            // this should not happen, but should be logged someday
            e.printStackTrace();
            return;
        }
        final short val = num.shortValue();
        if (val > fieldValue) {
            throw new IllegalArgumentException(String.format("%s (%d) je příliš velké (max = %d)", fieldName, val, fieldValue));
        }
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
        return pocatecniZasoby[pocetDruhuZbozi * druhZbozi + supermarket];
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
        return produkceTovaren[pocetDruhuZbozi * druhZbozi * pocetDni + pocetTovaren * den + tovarna];
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
        return poptavkaZbozi[0];
    }

    /**
     * Tato trida zaridi nemennost pole - nechceme nijak nikdy editovat raw data (kdyby nas to nekdy napadlo).
     * Data jsou tedy accessible, ale pouze jejich kopie.
     *
     * @author Jakub Smrha
     * @apiNote Zatim not used
     */
    public class Data {

        public final short[] cenaPrevozu = Arrays.copyOf(Storage.this.cenaPrevozu, Storage.this.cenaPrevozu.length);
        public final short[] pocatecniZasoby = Arrays.copyOf(Storage.this.pocatecniZasoby, Storage.this.pocatecniZasoby.length);
        public final short[] produkceTovaren = Arrays.copyOf(Storage.this.produkceTovaren, Storage.this.produkceTovaren.length);
        public final short[] poptavkaZbozi = Arrays.copyOf(Storage.this.poptavkaZbozi, Storage.this.poptavkaZbozi.length);
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
