package git.doomshade.semstralka.martin;

import git.doomshade.semstralka.Main;
import git.doomshade.semstralka.impl.graph.Storage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) throws IOException {
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
        /*
        double[][] arcMatrix = new double[][] {
                new double[] {20,20,0,0},
                new double[] {0,10,50,0},
                new double[] {0,0,0,50}
        };
        int[][] costMatrix = new int[][] {
                new int[] {4,6,8,8},
                new int[] {6,8,6,7},
                new int[] {5,7,6,8}
        };



        MODI modi = new MODI(arcMatrix, costMatrix);
        modi.calculateMODI();

         */

        Storage buhvi = Main.read(new File("C:\\Users\\jakub\\Downloads\\pt_2020_2021_data\\test_price.txt"));
        Simulation matrix = new Simulation(buhvi);

        System.out.printf("továrny:      %d \n" +
                          "supermarkety: %d \n" +
                          "zbozi:        %d \n" +
                          "dny:          %d \n", matrix.storage.pocetTovaren, matrix.storage.pocetSupermarketu, matrix.storage.pocetDruhuZbozi, matrix.storage.pocetDni);
        System.out.printf("zásoby:       %d \n" +
                          "produkce:     %d \n" +
                          "poptávka:     %d \n", matrix.totalStocks, matrix.totalProduction, matrix.totalDemand);

        System.out.println("\n<------------------------------------->\n");

        matrix.simulateNextDay();
    }
}
