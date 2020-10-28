package git.doomshade.semestralka.impl.graph.matrix;

import git.doomshade.semestralka.adt.Edge;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementace grafu maticí sousednosti
 *
 * @deprecated Nevyužije se
 */
@Deprecated(forRemoval = true)
public class NeighbouringMatrixGraph extends MatrixGraph {

    public NeighbouringMatrixGraph(boolean oriented, short invalidNumber) {
        super(oriented, invalidNumber);
    }

    public NeighbouringMatrixGraph(boolean oriented) {
        super(oriented);
    }

    @Override
    public List<? extends Number> neighbours(int vertex) {
        short[] nb = matrix[vertex];
        List<Short> neighbours = new ArrayList<>();

        for (short i = 0; i < nb.length; i++) {
            short num = nb[i];
            if (num != initNumber)
                neighbours.add(i);
        }
        return neighbours;
    }

    @Override
    public void initializeGraph(int size) {
        super.initializeGraph(size);
    }

    @Override
    public void addEdge(Edge edge) {
        matrix[edge.start][edge.end] = edge.value;

        if (!isOriented()) {
            matrix[edge.end][edge.start] = edge.value;
        }
    }
}
