package git.doomshade.semstralka.martin;

import java.util.ArrayList;
import java.util.List;

public class DetectCycle {

    final int[][] matrix;

    public DetectCycle(int[][] matrix) {
        this.matrix = matrix;
    }

    public List<Integer> detectCycle(int x, int y) {
        ArrayList<Integer> prev = new ArrayList<>();
        prev.add(x);
        prev.add(y);

        this.xTar = x;
        this.yTar = y;

        ArrayList<Integer> res = checkArc(x,y, Move.VERTICAL);
        return res;
    }

    /*
    private ArrayList<Integer> checkArc(int xPos, int yPos, ArrayList<Integer> prev, Move move) {
        // zkontroluj vertikalni pohyb
        if (move == Move.VERTICAL) {
            for (int x = 0; x < 4; x++) {
                //zkontroluj zda existuje na ose x aktivní hrana

                // nekontroluj tu samou hranu...
                if (x == xPos)
                    continue;
                //byla už hrana jednou kontrolována? (stačí kontrolovat jen počáteční hranu(doufejme...))
                if (x == prev.get(0) && yPos == prev.get(1)) {
                    prev.add(x);
                    prev.add(yPos);

                    return prev;
                }

                //nasel jsi neprázdnou hranu?
                if (matrix[yPos][x] >= 1) {
                    prev.add(x);
                    prev.add(yPos);

                    prev = checkArc(x,yPos, prev, Move.HORIZONTAL);
                }
            }
        }
        else {
            for (int y = 0; y < 3; y++) {
                //zkontroluj zda existuje na ose y aktivní hrana

                // nekontroluj tu samou hranu...
                if (y == yPos)
                    continue;

                //byla už hrana jednou kontrolována? (stačí kontrolovat jen počáteční hranu(doufejme...))
                if (xPos == prev.get(0) && y == prev.get(1)) {
                    prev.add(xPos);
                    prev.add(y);

                    return prev;
                }

                //nasel jsi neprázdnou hranu?
                if (matrix[y][xPos] >= 1) {
                    prev.add(xPos);
                    prev.add(y);

                    prev = checkArc(xPos,y, prev, Move.VERTICAL);
                }
            }
        }
        return prev;
    }
     */

    int xTar;
    int yTar;
    private ArrayList<Integer> checkArc(int xPos, int yPos, Move move) {
        ArrayList<Integer> arr = null;
        if (move == Move.VERTICAL) {
            for (int x = 0; x < 4; x++) {
                if (x == xPos)
                    continue;
                if (x == xTar && yPos == yTar) {
                    //escape
                    ArrayList<Integer> res = new ArrayList<>();
                    res.add(x);
                    res.add(yPos);

                    return res;
                }
                if (matrix[yPos][x] >= 1) {
                    arr = checkArc(x,yPos, Move.HORIZONTAL);
                    if (arr != null) {
                        arr.add(x);
                        arr.add(yPos);
                        return arr;
                    }
                }
            }
        }
        else {
            for (int y = 0; y < 3; y++) {
                if (y == yPos)
                    continue;
                if (xPos == xTar && y == yTar) {
                    //escape
                    ArrayList<Integer> res = new ArrayList<>();
                    res.add(xPos);
                    res.add(y);

                    return res;
                }
                if (matrix[y][xPos] >= 1) {
                    arr = checkArc(xPos,y, Move.VERTICAL);
                    if (arr != null) {
                        arr.add(xPos);
                        arr.add(y);
                        return arr;
                    }
                }
            }
        }

        return arr;
    }
}
