package git.doomshade.semstralka.impl.graph.search;

/**
 * @deprecated Nevyužije se
 */
@Deprecated(forRemoval = true)
public class Search {

    /*public static <T> T[] BFS(int s, Number[][] matrix, Function<Number, T> function) {
        Object[] result = new Object[matrix[0].length];

        Arrays.fill(result, -1);

        result[s] = 0;

        int[] mark = new int[matrix[0].length];
        mark[s] = -1;

        Deque<Integer> q = new LinkedList<>();

        q.add(s);

        while (!q.isEmpty()) {
            int v = q.poll();

            final List<Integer> nbs = neighbours(v, matrix);

            for (int n : nbs) {
                if (mark[n] == 0) {
                    mark[n] = 1;
                    q.add(n);
                }
            }
            mark[v] = 2;
        }
    }*/

    /*
    private static List<Integer> neighbours(int v, Number[][] matrix) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < matrix[0].length; i++) {
            if (matrix[v][i].longValue() == 1) result.add(i);
        }
        return result;
    }*/
}
