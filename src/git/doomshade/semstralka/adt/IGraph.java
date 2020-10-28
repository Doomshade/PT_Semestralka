package git.doomshade.semstralka.adt;

import java.util.List;

/**
 * Rozhraní ADT Grafu
 *
 * @deprecated Nevyužije se
 */
@Deprecated(forRemoval = true)
public interface IGraph {

    /**
     * @param vertex vrchol v
     * @return sousedy vrcholu v
     */
    List<? extends Number> neighbours(int vertex);

    /**
     * Nainicializuje graf na danou velikost
     *
     * @param size velikost grafu
     */
    void initializeGraph(int size);

    /**
     * Přidá hranu do grafu
     *
     * @param edge hrana, kterou přidat
     */
    void addEdge(Edge edge);

    /**
     * @return {@code true}, pokud je graf orientovaný, jinak {@code false}
     */
    boolean isOriented();

}
