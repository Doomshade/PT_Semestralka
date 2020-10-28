package git.doomshade.semestralka.smrha;

/**
 * @author Jakub Šmrha
 * @version 1.0 (27.10.2020)
 */
public class NWSolution extends Solution {
    public NWSolution(short[][] matrix, short[] supply, short[] demand) {
        super(matrix, supply, demand);
    }

    @Override
    protected void solveProblem() {
        System.out.println();
        System.out.println("-----------------------------------------");
        northWest(0, 0);
    }

    private void northWest(int x, int y) {
        // musime se kouknout, zda je x a y in bounds
        // pokud neni, tak nechceme delat nic
        if (x >= costMatrix[0].length || y >= costMatrix.length) return;


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
        // z enum indexu ziskame deletedPart (row/column/both)

        DeletedPart deletedPart = choose(x, y);
        System.out.println("DeletedPart " + deletedPart);
        System.out.println("-----------------------------------------");

        switch (deletedPart) {
            case ROW -> northWest(x, y + 1);
            case COLUMN -> northWest(x + 1, y);
            case BOTH -> northWest(x + 1, y + 1);
        }
    }
}
