package git.doomshade.semestralka.impl.graph;

import git.doomshade.semestralka.adt.IGraph;

import java.io.PrintStream;

/**
 * Základní implementace grafu
 *
 * @author Jakub Šmrha
 * @deprecated Nevyužije se
 */
@Deprecated(forRemoval = true)
public abstract class Graph implements IGraph {

    /**
     * Určuje, zda je graf orientovaný
     */
    private final boolean oriented;

    /**
     * <p>Konstruktor grafu
     * <p>Na základě toho, zda je graf orientovaný, je dále upravena implementace v metodách
     *
     * @param oriented zda je graf orientovaný
     */
    public Graph(boolean oriented) {
        this.oriented = oriented;
    }

    @Override
    public final boolean isOriented() {
        return oriented;
    }

    /**
     * Metoda pro výpis do print streamu, je optional na dědění, proto není abstract ani final
     *
     * @param out kam vypsat graf
     */
    public void print(PrintStream out) {
    }

    /*
    public <T> T DFS(T shit) {
        return null;
    }

    public <T> T BFS(T shit) {
        return null;
    }*/
}
