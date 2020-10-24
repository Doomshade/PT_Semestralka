package git.doomshade.semstralka.impl.graph.linked;

import git.doomshade.semstralka.adt.Edge;
import git.doomshade.semstralka.impl.graph.Graph;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Seznamová implementace grafu
 */
@Deprecated(forRemoval = true)
public class LinkedListGraph extends Graph {
    private LinkedList<Edge>[] list;

    public LinkedListGraph(boolean oriented) {
        super(oriented);
    }

    @Override
    public void initializeGraph(int size) {
        list = new LinkedList[size];

        for (int i = 0; i < size; i++) {
            list[i] = new LinkedList<>();
        }


        /*for (int i = 0; i < supermarkety; i++) {
            for (int j = 0; j < tovarny; j++) {
                data.getCena(i, j);
                Link link = new Link(0,data.getCena(i,j),edges[0]);

            }
        }*/
    }

    @Override
    public void addEdge(Edge edge) {
        list[edge.start].addFirst(edge);
        if (!isOriented()) {
            list[edge.end].addFirst(edge);
        }
    }

    @Override
    public List<? extends Number> neighbours(int vertex) {
        return list[vertex].stream().map(x -> x.end).collect(Collectors.toList());
    }

    @Override
    public void print(PrintStream out) {
        for (LinkedList<Edge> edges : this.list) {
            out.println(edges);
        }
    }
}
