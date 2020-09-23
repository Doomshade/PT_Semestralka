package git.doomshade.semstralka.graph;

public abstract class Graph {
    protected final Storage data;

    public Graph(Storage data) {
        this.data = data;
        fillData();
    }

    /**
     * Treba se to bude hodit, WHO KNOWS
     */
    protected abstract void fillData();

    public <T> T DFS(T shit) {
        return null;
    }

    public <T> T BFS(T shit) {
        return null;
    }
}
