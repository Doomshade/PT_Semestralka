package git.doomshade.semstralka.util;

import java.io.PrintStream;
import java.util.Arrays;

public class MatrixUtil {

    public static void print(Object[][] matrix, PrintStream out) {
        for (Object[] objects : matrix) {
            out.println(Arrays.toString(objects));
        }
    }
}
