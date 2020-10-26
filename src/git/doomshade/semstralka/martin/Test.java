package git.doomshade.semstralka.martin;

public class Test {

    public static void main(String[] args) {
        /*
        DetectCycle dc = new DetectCycle();

        ArrayList<Integer> cycle = (ArrayList<Integer>) dc.detectCycle(1,0);
        if (cycle != null)
            for (int i = 0; i < cycle.size(); i+=2) {
                System.out.println(cycle.get(i) + ";" + cycle.get(i+1));
            }
        else {
            System.out.println("žádný cyklus");
        }
         */
        /*
        UVMethod uvMethod = new UVMethod();
        uvMethod.calculate();
        */
        /*
        for (int i = 0; i < uvMethod.unallocated.size(); i++) {
            System.out.println("[" + uvMethod.unallocated.get(i)[0] + ";" + uvMethod.unallocated.get(i)[1] + "] -> " + res[i]);
        }


         */
        /*
        for(double num : uvMethod.u) {
            System.out.print(num + " ");
        }
        System.out.println();
        for (double num : uvMethod.v) {
            System.out.print(num + " ");
        }
         */
        int[][] arcMatrix = new int[][] {
                new int[] {20,20,0,0},
                new int[] {0,0,50,10},
                new int[] {0,10,0,40}
        };
        int[][] costMatrix = new int[][] {
                new int[] {4,6,8,8},
                new int[] {6,8,6,7},
                new int[] {5,7,6,8}
        };

        MODI modi = new MODI(arcMatrix, costMatrix);
        modi.calculateMODI();
    }
}
