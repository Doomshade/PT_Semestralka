package git.doomshade.semestralka.impl.graph.matrix;

import git.doomshade.semestralka.adt.Edge;

import java.util.List;

/**
 * Implementace grafu incidenční maticí
 *
 * @deprecated Nevyužije se
 */
@Deprecated(forRemoval = true)
public class IncidentialMatrixGraph extends MatrixGraph {

    public IncidentialMatrixGraph(boolean oriented, short invalidNumber) {
        super(oriented, invalidNumber);
    }

    public IncidentialMatrixGraph(boolean oriented) {
        super(oriented);
    }

    @Override
    public List<Integer> neighbours(int vertex) {
        throw new UnsupportedOperationException("Ještě není implementováno :)");
    }

    @Override
    public void initializeGraph(int size) {
        super.initializeGraph(size);
        //Storage.Data dataArrays = data.getData();
        /*
        final short tovarny = data.pocetTovaren;
        final short supermarkety = data.pocetSupermarketu;


        for (int i = 0; i < tovarny; i++) {
            for (int j = 0; j < supermarkety; j++) {
                matrix[i][j] = dataArrays.cenaPrevozu[i * tovarny + j];
            }
        }*/
    }

    @Override
    public void addEdge(Edge edge) {
        throw new UnsupportedOperationException("Ještě není implementováno :)");
    }
}
