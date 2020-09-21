package git.doomshade.semstralka;

import java.lang.reflect.Field;

/**
 * Trida, ve ktere jsou reprezentovana data v jednodimenzionalnim poli. Data lze precist pomoci pomocnych get metod
 *
 * @author Jakub Smrha
 */
public class Data {

    /**
     * Pole dat, postaci short (setrime pamet)
     */
    private final short[] cenaPrevozu, pocatecniZasoby, produkceTovaren, poptavkaZbozi;

    /**
     * Velikost pole dat, postaci short (setrime pamet)
     */
    private final short d, s, z, t;

    public Data(short[] cenaPrevozu, short[] pocatecniZasoby, short[] produkceTovaren, short[] poptavkaZbozi, short d, short s, short z, short t) {
        this.cenaPrevozu = cenaPrevozu;
        this.pocatecniZasoby = pocatecniZasoby;
        this.produkceTovaren = produkceTovaren;
        this.poptavkaZbozi = poptavkaZbozi;
        this.d = d;
        this.s = s;
        this.z = z;
        this.t = t;
    }

    /**
     * Validuje input (neni pravdepodobne nutny, ale just to make sure)
     *
     * @param fieldName s jakym variablem to porovnat (pointery pls :( )
     * @param num       cislo
     * @throws IllegalArgumentException pokud je cislo > 0
     */
    private void validate(String fieldName, Number num) throws IllegalArgumentException {
        short fieldValue;
        try {
            final Field declaredField = getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            fieldValue = declaredField.getShort(this);
        } catch (Exception e) {
            // this should ot happen, but should be logged someday
            e.printStackTrace();
            return;
        }
        final short val = num.shortValue();
        if (val > fieldValue) {
            throw new IllegalArgumentException(String.format("%s (%d) je prilis velke (max = %d)", fieldName, val, fieldValue));
        }
    }

    /**
     * @param supermarket supermarket
     * @param tovarna     tovarna
     * @return cenu na zaklade supermarketu a tovarny
     * @throws IllegalArgumentException pokud bude zadano spatne cislo
     */
    public int getCena(int supermarket, int tovarna) throws IllegalArgumentException {
        validate("s", supermarket);
        validate("d", tovarna);
        return cenaPrevozu[s * supermarket + tovarna];
    }

    public int getZasoby(int druhZbozi, int supermarket) throws IllegalArgumentException {
        validate("z", druhZbozi);
        validate("s", supermarket);
        return pocatecniZasoby[z * druhZbozi + supermarket];
    }

    public int getProdukce(int druhZbozi, int tovarna, int den) throws IllegalArgumentException {
        validate("z", druhZbozi);
        validate("d", tovarna);
        validate("t", den);

        // hope this is correct
        final int idx = z * druhZbozi * t + d * den + tovarna;

        System.out.println(idx);
        return produkceTovaren[idx];
    }

    public int getPoptavka(int supermarket, int den, int druhZbozi) throws IllegalArgumentException {

        return poptavkaZbozi[0];
    }


}
