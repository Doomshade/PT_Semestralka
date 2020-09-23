package git.doomshade.semstralka.graph;

/**
 * Maticova implementace grafu
 */
public class MatrixGraph extends Graph {

    private short[][] matrix;

    public MatrixGraph(Storage data) {
        super(data);
    }

    @Override
    protected void fillData() {
        final short tovarny = data.pocetTovaren;
        final short supermarkety = data.pocetSupermarketu;

        matrix = new short[tovarny][supermarkety];

        Storage.Data dataArrays = data.getData();

        for (int i = 0; i < tovarny; i++) {
            for (int j = 0; j < supermarkety; j++) {
                matrix[i][j] = dataArrays.cenaPrevozu[i * tovarny + j];
            }
        }
    }
}
