package git.doomshade.semstralka.martin;

/**
 * Třída počítající u a v MODI algoritmu
 * https://www.youtube.com/watch?v=dQw4w9WgXcQ
 *
 * @author Intellij IDEA community edition
 */
public class UV {

    //-- PMD territory -- Error Logic not found... --  Enter At Your Own Risk --

    private final MODI modi;

    public UV(MODI modi) {
        this.modi = modi;
    }

    /**
     * Vypočítá, pokud je to možné u a v atributy algoritmu
     *
     * @return true pokud přiřazeno, jinak false
     */
    boolean calculateUVs() {
        boolean impossible = false;
        while (!modi.allUVsSet()) {
            if (impossible) {
                return false;
            }
            impossible = isImpossible();
        }
        return true;
    }

    /**
     * Zjištujě zda je možné vypočítat všechna u a v a pořítá je i rovnou
     *
     * @return true -> pokud se podařilo, false když ne
     */
    private boolean isImpossible() {
        boolean impossible = false;
        for (int[] arc : modi.allocated) {
            int xCoord = arc[0];
            int yCoord = arc[1];
            // zjisti zda známe alespoň jednu proměnnou
            if (modi.u[yCoord] == Double.POSITIVE_INFINITY && modi.v[xCoord] == Double.POSITIVE_INFINITY) {
                impossible = true;
            }
            if (modi.u[yCoord] != Double.POSITIVE_INFINITY && modi.v[xCoord] == Double.POSITIVE_INFINITY) {
                // známe u[y]
                modi.v[xCoord] = modi.costMatrix[yCoord][xCoord] - modi.u[yCoord];
                impossible = false;
                continue;
            }
            if (modi.v[xCoord] != Double.POSITIVE_INFINITY && modi.u[yCoord] == Double.POSITIVE_INFINITY) {
                modi.u[yCoord] = modi.costMatrix[yCoord][xCoord] - modi.v[xCoord];
                impossible = false;
                continue;
            }
        }
        return impossible;
    }

    /**
     * Snaž se vypočítat u a v když je degenerativní řešení
     *
     * @return true -> pokud ano jina false
     */
    boolean performUVs() {
        if (!calculateUVs()) {
            // degenerativní řešení
            if (!modi.calculateEpsilons()) {
                return true;
            }//NEMOHL JSEM NENAJÍT ŘEŠENÍ BEZ CYKLU -> nemohu provést algoritmus
            modi.getAllocations(); // restartuj alokace
            // FATAL ERROR -> tohle snad ani nemůže nastat
            return !calculateUVs();

            /*
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 4; x++) {
                    if (arcMatrix[y][x] == Double.POSITIVE_INFINITY) {
                        System.out.print("E ");
                        continue;
                    }
                    System.out.print(arcMatrix[y][x] + " ");
                }
                System.out.println();
            }
             */
        }
        return false;
    }
}
