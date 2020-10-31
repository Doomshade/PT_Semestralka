package git.doomshade.semstralka.martin;

import java.util.ArrayList;
import java.util.Arrays;

public class UVMethod {

    int[][] valueMatrix = new int[][] {
            new int[] {20, 20, 0, 0},
            new int[] {0, 0, 50, 10},
            new int[] {0, 10, 0, 40}
    };
    int[][] costMatrix = new int[][] {
            new int[] {4,6,8,8},
            new int[] {6,8,6,7},
            new int[] {5,7,6,8}
    };

    public double[] u = new double[3]; // očekávám 3
    public double[] v = new double[4]; // očekávám 4
    private void initUV() {
        Arrays.fill(u, Double.POSITIVE_INFINITY);
        Arrays.fill(v, Double.POSITIVE_INFINITY);
        u[0] = 0;
    }

    ArrayList<int[]> allocated;
    ArrayList<int[]> unallocated;

    /**
     * Zatím jen pro nedegenerované řešení... :D
     *
     * očekávám že se začíná s basic feasible solution
     */
    public void calculateUVVariables() {
        initUV();
        getAllocations();

        // máme alokace, teď potřeba vypočítat u a v
        // dokud nejsou vypočítany všechna u a v iteruj dokud nejsou
        boolean computed = false;
        while(!allUVSet()) {
            for (int[] coord : allocated) {
                int x = coord[0];
                int y = coord[1];
                // zjisti zda známe alespoň jednu proměnnou
                if (u[y] == Double.POSITIVE_INFINITY && v[x] == Double.POSITIVE_INFINITY)
                    continue;
                if (u[y] != Double.POSITIVE_INFINITY) {
                    // známe u[y]
                    v[x] = costMatrix[y][x] - u[y];
                    continue;
                }
                if (v[x] != Double.POSITIVE_INFINITY) {
                    u[y] = costMatrix[y][x] - v[x];
                }
            }
        }
    }
    private boolean allUVSet() {
        for (double num : u) {
            if (num == Double.POSITIVE_INFINITY)
                return false;
        }
        for (double num : v) {
            if (num == Double.POSITIVE_INFINITY)
                return false;
        }

        return true;
    }

    public int[] calculateNetIncrease() {
        int[] res = new int[unallocated.size()];

        for (int i = 0; i < unallocated.size(); i++) {
            int x = unallocated.get(i)[0];
            int y = unallocated.get(i)[1];

            res[i] = (int) (costMatrix[y][x]  - (u[y] + v[x]));
        }

        return res;
    }

    public void getAllocations() {
        allocated = new ArrayList<>();
        unallocated = new ArrayList<>();

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                if (valueMatrix[y][x] > 0)
                    allocated.add(new int[] {x,y});
                else
                    unallocated.add(new int[] {x,y});
            }
        }
    }

    public void calculate() {
        getAllocations(); // zjisti rozdeleni na aktivni hrany a neaktivni

        initUV(); // inicializuj u a v pole
        calculateUVVariables(); // vypočítej u a v pole

        int[] netIncrease = calculateNetIncrease(); // vypočítej net increase
        int min = 0;
        for (int i = 0; i < netIncrease.length; i++) {
            if (netIncrease[i] < netIncrease[min]) {
                min = i;
            }
        }
        if (netIncrease[min] < 0) {
            int[][] updated = Arrays.copyOf(valueMatrix, valueMatrix.length);
            int x = unallocated.get(min)[0];
            int y = unallocated.get(min)[1];
            updated[y][x] = 1;
            DetectCycle dc = new DetectCycle(updated);
            ArrayList<Integer> cycle = (ArrayList<Integer>) dc.detectCycle(x,y);
            if (cycle != null) {
                updated[y][x] = 0;
                int[] netDecrease = new int[cycle.size() / 2];
                int mul = 1;
                int pos = 0;
                for (int i = 0; i < cycle.size(); i+=2) {
                    int xPos = cycle.get(i);
                    int yPos  = cycle.get(i + 1);

                    int goods = valueMatrix[yPos][xPos];
                    netDecrease[pos] = goods * mul;
                    pos++;
                    mul *= -1;
                }

                int theta = netDecrease[1];
                for (int num : netDecrease) {
                    if (num < 0 && num > theta) {
                        theta = num;
                    }
                }

                ArrayList<int[]> coords = new ArrayList<>();
                for (int i = 0; i < cycle.size(); i += 2) {
                    coords.add(new int[] {cycle.get(i), cycle.get(i+1)});
                }

                mul = 1;
                for (int[] cor : coords) {
                    valueMatrix[cor[1]][cor[0]] += (theta*-1) * mul;
                    //System.out.print(cor[0] + " " + cor[1]);
                    mul *= -1;
                }

                for (int a = 0; a < 3; a++) {
                    for (int b = 0; b < 4; b++) {
                        System.out.print(valueMatrix[a][b] + " ");
                    }
                    System.out.println();
                }
            }
        }
    }
}
