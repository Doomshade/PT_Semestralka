package git.doomshade.semstralka.martin;

import java.util.ArrayList;
import java.util.List;

/**
 * Třída dokáže najít cyklus, v 2d poli, který splňuje náležitosti cyklu v "transportation problem"
 *
 * @author Martin Jakubašek
 */
public class CycleDetection {

    /** matice kde mame detekovat cyklus */
    private final double[][] matrix;

    /**
     * Vytvoří novou instanci třídy, určenou pro hledání v konkrétní matici zadané jako parametr
     *
     * @param matrix matice splňující náležitosti "transportation problem"
     */
    public CycleDetection(double[][] matrix) {
        this.matrix = matrix;
    }

    //--

    /**
     * x
     */
    private int xTar;
    /**
     * y
     */
    private int yTar;

    /**
     * Metoda detekuje, zda existuje cyklus v matici ze zadané pozice
     * 1 0 1   1 1 0
     * ukázka cyklu:    0 0 0 , 0 1 0
     * 1 0 1   1 1 1
     *
     * @param xCoord souřadnice x v matici
     * @param yCoord souřadnice y v matici
     * @return pokud cyklus existuje vrátí list, ve formátu, kdy jeden prvek listu je pole souřadnic prvku [x,y], jinak vrátí null
     */
    public List<int[]> detectCycle(int xCoord, int yCoord) {
        this.xTar = xCoord;
        this.yTar = yCoord;

        List<int[]> cycle = checkArcVertical(xCoord, yCoord);
        //Collections.reverse(cycle);
        if (cycle == null) {
            return null;
        }
        return cycle.size() < 4 ? null : cycle;
    }


    /*
     * Rekurzivní metoda postupně prohledá všechny souřadnice, jejichž obsah je nenulový, tak dlouho dokud nenalezne cyklus.
     * V případě že cyklus nenalezne vrátí null nebo list o velikosti menší než 4.
     *
     * @param xCoord souřadnice x v matici
     * @param yCoord souřadnice y v matici
     * @param move   pohyb, buď horizontální nebo vertikální
     * @return pole prvků tvořící cyklus nebo null (může nastat i situace vrácení listu o velikosti menší než 4)
     */
    /*
    private List<int[]> checkArc(int xCoord, int yCoord, Move move) {
        List<int[]> arr = null;
        if (move == Move.VERTICAL) {
            for (int x = 0; x < matrix[0].length; x++) {
                if (x == xCoord) {
                    continue;
                }
                List<int[]> res = getRes(yCoord, x);
                if (res != null) {
                    return res;
                }
                if (matrix[yCoord][x] >= 1) {
                    arr = checkArc(x, yCoord, Move.HORIZONTAL);
                    if (arr != null) {
                        arr.add(new int[]{x, yCoord});
                        return arr;
                    }
                }
            }
        } else {
            for (int y = 0; y < matrix.length; y++) {
                if (y == yCoord) {
                    continue;
                }
                List<int[]> res = getRes(y, xCoord);
                if (res != null) {
                    return res;
                }
                if (matrix[y][xCoord] >= 1) {
                    arr = checkArc(xCoord, y, Move.VERTICAL);
                    if (arr != null) {
                        arr.add(new int[]{xCoord, y});
                        return arr;
                    }
                }
            }
        }

        return arr;
    }
     */

    /**
     * Rekurzivní metoda postupně prohledá všechny souřadnice, jejichž obsah je nenulový, tak dlouho dokud nenalezne cyklus.
     * V případě že cyklus nenalezne vrátí null nebo list o velikosti menší než 4.
     *
     * @param xCoord souřadnice x v matici
     * @param yCoord souřadnice y v matici
     * @return pole prvků tvořící cyklus nebo null (může nastat i situace vrácení listu o velikosti menší než 4)
     */
    private List<int[]> checkArcVertical(int xCoord, int yCoord) {
        List<int[]> arr = null;
        for (int x = 0; x < matrix[0].length; x++) {
            if (x == xCoord) {
                continue;
            }

            List<int[]> res = getRes(yCoord, x);

            if (res != null) {
                return res;
            }
            if (matrix[yCoord][x] >= 1) {
                arr = checkArcHorizontal(x, yCoord);
                if (arr != null) {
                    arr.add(new int[]{x, yCoord});
                    return arr;
                }
            }
        }
        return arr;
    }

    /**
     * Rekurzivní metoda postupně prohledá všechny souřadnice, jejichž obsah je nenulový, tak dlouho dokud nenalezne cyklus.
     * V případě že cyklus nenalezne vrátí null nebo list o velikosti menší než 4.
     *
     * @param xCoord souřadnice x v matici
     * @param yCoord souřadnice y v matici
     * @return pole prvků tvořící cyklus nebo null (může nastat i situace vrácení listu o velikosti menší než 4)
     */
    private List<int[]> checkArcHorizontal(int xCoord, int yCoord) {
        List<int[]> arr = null;
        for (int y = 0; y < matrix.length; y++) {
            if (y == yCoord) {
                continue;
            }
            List<int[]> res = getRes(y, xCoord);
            if (res != null) {
                return res;
            }
            if (matrix[y][xCoord] >= 1) {
                arr = checkArcVertical(xCoord, y);
                if (arr != null) {
                    arr.add(new int[]{xCoord, y});
                    return arr;
                }
            }
        }
        return arr;
    }

    /**
     * Vrátí výsledné pole cyklu z rekurzivního algoritmu
     *
     * @param yCoord souřadnice y
     * @param x      souřadnice x
     * @return výsledné pole souřadnic prvků cyklu nebo null
     */
    private List<int[]> getRes(int yCoord, int x) {
        if (x == xTar && yCoord == yTar) {
            //escape
            ArrayList<int[]> res = new ArrayList<>();
            res.add(new int[]{x, yCoord});
            return res;
        }
        return null;
    }
}
