package git.doomshade.semstralka.graph;

/**
 * @apiNote Will likely not be used, but shall remain for now
 */
public class LinkedListGraph extends Graph {
    private Link[] edges;

    private static class Link {
        int neighbour;
        double value;
        Link next;

        private Link(int neighbour, double value, Link next) {
            this.neighbour = neighbour;
            this.value = value;
            this.next = next;
        }
    }

    public LinkedListGraph(Storage data) {
        super(data);
    }

    @Override
    protected void fillData() {
        final short tovarny = data.pocetTovaren;
        final short supermarkety = data.pocetSupermarketu;
        edges = new Link[supermarkety * tovarny];

        /*for (int i = 0; i < supermarkety; i++) {
            for (int j = 0; j < tovarny; j++) {
                data.getCena(i, j);
                Link link = new Link(0,data.getCena(i,j),edges[0]);

            }
        }*/
    }
}
