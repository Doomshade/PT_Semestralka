package git.doomshade.semstralka.util;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.BiPredicate;

/**
 * Třída pro různé utility pro matice
 *
 * @author Jakub Šmrha
 * @version 1.0
 */
public final class MatrixUtil {

    private MatrixUtil(){}

    ///////////////////////////////////////////////////////////////////////////
    // Print metody
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Vypíše do out matici
     *
     * @param matrix matice
     * @param out    out
     */
    public static void print(Object[][] matrix, PrintStream out) {
        out.println(Arrays.deepToString(matrix));
    }

    /**
     * Vypíše do out matici
     *
     * @param matrix matice
     * @param out    out
     */
    public static void print(short[][] matrix, PrintStream out) {
        out.println(Arrays.deepToString(matrix));
    }

    /**
     * Vypíše do out matici
     *
     * @param matrix matice
     * @param out    out
     */
    public static void print(int[][] matrix, PrintStream out) {
        out.println(Arrays.deepToString(matrix));

    }

    /**
     * Vypíše do out matici
     *
     * @param matrix matice
     * @param out    out
     */
    public static void print(double[][] matrix, PrintStream out) {
        out.println(Arrays.deepToString(matrix));
    }


    ///////////////////////////////////////////////////////////////////////////
    // Metody na vytváření matice
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @param dataLen délka dat
     * @param xLen    délka jednoho pole
     * @return y délku matice
     * @throws IllegalArgumentException pokud nelze vytvořit z těchto čísel matici
     */
    private static int getYLen(int dataLen, int xLen) throws IllegalArgumentException {
        if (dataLen % xLen != 0)
            throw new IllegalArgumentException(
                    String.format(
                            "Invalid data length, cannot create matrix (data len = %d, xLen = %d)",
                            dataLen,
                            xLen));
        return dataLen / xLen;
    }

    /**
     * Vytvoří matici z pole dat
     *
     * @param data pole dat
     * @param xLen šířka matice
     * @param <T>  typ matice
     * @return matici z pole dat
     */
    private static <T> T doCreateMatrix(Object data, int xLen) throws IllegalArgumentException {
        if (!data.getClass().isArray()) throw new IllegalArgumentException(data + " is not an array!");

        // java :)
        final Object[] arr = (Object[]) data;

        // well... Array.newInstance nám vytvoří matici o typu T m/n, kdy m = xLen a n = yLen
        return (T) Array.newInstance(arr[0].getClass(), getYLen(arr.length, xLen), xLen);
    }

    /**
     * Vytvoří generický typ matice m/n, kdy m = xLen
     *
     * @param data data
     * @param xLen délka pole (m)
     * @param <T>  typ matice
     * @return matici z dat
     * @throws IllegalArgumentException pokud nelze vytvořit matici
     */
    public static <T> T[][] createMatrix(T[] data, int xLen) throws IllegalArgumentException {

        // nemusíme přetypovávat na konci na T[][] díky této metodě
        final T[][] matrix = doCreateMatrix(data, xLen);

        // bohužel, tento úsek se musí kopírovat, nelze přetypovat Object[][] na short[][] a vice versa
        for (int i = 0; i < xLen; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[j][i] = data[i * matrix.length + j];
            }
        }

        return matrix;
    }

    /**
     * Vytvoří matici m/n, kdy m = xLen
     *
     * @param data data
     * @param xLen délka pole (m)
     * @return matici z dat
     * @throws IllegalArgumentException pokud nelze vytvořit matici
     */
    public static short[][] createMatrix(short[] data, int xLen) throws IllegalArgumentException {
        final int yLen = getYLen(data.length, xLen);

        final short[][] matrix = new short[yLen][xLen];

        for (int i = 0; i < xLen; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[j][i] = data[i * matrix.length + j];
            }
        }
        return matrix;
    }

    /**
     * Vytvoří matici m/n, kdy m = xLen
     *
     * @param data data
     * @param xLen délka pole (m)
     * @return matici z dat
     * @throws IllegalArgumentException pokud nelze vytvořit matici
     */
    public static int[][] createMatrix(int[] data, int xLen) throws IllegalArgumentException {
        final int yLen = getYLen(data.length, xLen);

        final int[][] matrix = new int[yLen][xLen];

        for (int i = 0; i < xLen; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[j][i] = data[i * matrix.length + j];
            }
        }
        return matrix;
    }

    /**
     * Vytvoří matici m/n, kdy m = xLen
     *
     * @param data data
     * @param xLen délka pole (m)
     * @return matici z dat
     * @throws IllegalArgumentException pokud nelze vytvořit matici
     */
    public static double[][] createMatrix(double[] data, int xLen) throws IllegalArgumentException {
        final int yLen = getYLen(data.length, xLen);

        final double[][] matrix = new double[yLen][xLen];

        for (int i = 0; i < xLen; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[j][i] = data[i * matrix.length + j];
            }
        }
        return matrix;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Metody na vyhledávání v matici
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Podmínka pro nejmenší celé číslo
     */
    public static final BiPredicate<Number, Number> LOWEST_INTEGER = (x, y) -> x.intValue() < y.intValue();

    /**
     * Podmínka pro největší celé číslo
     */
    public static final BiPredicate<Number, Number> HIGHEST_INTEGER = (x, y) -> x.intValue() > y.intValue();

    /**
     * Podmínka pro nejmenší reálné číslo
     */
    public static final BiPredicate<Number, Number> LOWEST_DOUBLE = (x, y) -> x.doubleValue() < y.doubleValue();

    /**
     * Podmínka pro největší reálné číslo
     */
    public static final BiPredicate<Number, Number> HIGHEST_DOUBLE = (x, y) -> x.doubleValue() > y.doubleValue();

    public static final boolean HIGHEST_NUMBER = false, LOWEST_NUMBER = true;

    /*
        Například chceme najít nejmenší číslo v short[][] matici
        Zavoláme search(matice, LOWEST_INTEGER, LOWEST_NUMBER)

        Nyní chceme největší číslo v double[][] matici
        Zavoláme search(matice, HIGHEST_DOUBLE, HIGHEST_NUMBER)

        That easy

     */

    /**
     * Pokusí se najít v matici číslo na základě podmínek
     *
     * @param matrix       matice, ve které se bude hledat
     * @param biPredicate  podmínka pro číslo
     * @param defaultValue defaultní číslo
     * @param <T>          typ
     * @return číslo na základě podmínek, pokud nenajde, vrátí defaultValue
     * @see MatrixUtil#LOWEST_INTEGER
     * @see MatrixUtil#HIGHEST_INTEGER
     * @see MatrixUtil#LOWEST_DOUBLE
     * @see MatrixUtil#HIGHEST_DOUBLE
     */
    public static <T extends Number> T search(T[][] matrix, BiPredicate<Number, Number> biPredicate, T defaultValue) {
        T val = defaultValue;
        for (T[] arr : matrix) {
            for (T t : arr) {
                if (biPredicate.test(t, val)) {
                    val = t;
                }
            }
        }
        return val;
    }

    /**
     * Pokusí se najít v matici číslo na základě podmínek
     *
     * @param matrix           matice, ve které se bude hledat
     * @param biPredicate      podmínka pro číslo
     * @param positiveInfinity {@code true} znamená, že defaultní číslo bude největší možné a {@code false} znamená nejmenší
     *                         (pro vyhledávání nejměnšího čísla by toto mělo být {@code true} a pro největší číslo {@code false})
     * @return číslo na základě podmínek
     * @see MatrixUtil#LOWEST_INTEGER
     * @see MatrixUtil#HIGHEST_INTEGER
     * @see MatrixUtil#LOWEST_DOUBLE
     * @see MatrixUtil#HIGHEST_DOUBLE
     */
    public static int search(int[][] matrix, BiPredicate<Number, Number> biPredicate, boolean positiveInfinity) {
        int num = positiveInfinity ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        for (int[] arr : matrix) {
            for (int s : arr) {
                if (biPredicate.test(s, num)) {
                    num = s;
                }
            }
        }
        return num;
    }

    /**
     * Pokusí se najít v matici číslo na základě podmínek
     *
     * @param matrix           matice, ve které se bude hledat
     * @param biPredicate      podmínka pro číslo
     * @param positiveInfinity {@code true} znamená, že defaultní číslo bude největší možné a {@code false} znamená nejmenší
     *                         (pro vyhledávání nejměnšího čísla by toto mělo být {@code true} a pro největší číslo {@code false})
     * @return číslo na základě podmínek
     * @see MatrixUtil#LOWEST_INTEGER
     * @see MatrixUtil#HIGHEST_INTEGER
     * @see MatrixUtil#LOWEST_DOUBLE
     * @see MatrixUtil#HIGHEST_DOUBLE
     */
    public static short search(short[][] matrix, BiPredicate<Number, Number> biPredicate, boolean positiveInfinity) {
        short num = positiveInfinity ? Short.MAX_VALUE : Short.MIN_VALUE;

        for (short[] arr : matrix) {
            for (short s : arr) {
                if (biPredicate.test(s, num)) {
                    num = s;
                }
            }
        }
        return num;
    }

    /**
     * Pokusí se najít v matici číslo na základě podmínek
     *
     * @param matrix           matice, ve které se bude hledat
     * @param biPredicate      podmínka pro číslo
     * @param positiveInfinity {@code true} znamená, že defaultní číslo bude největší možné a {@code false} znamená nejmenší
     *                         (pro vyhledávání nejměnšího čísla by toto mělo být {@code true} a pro největší číslo {@code false})
     * @return číslo na základě podmínek
     * @see MatrixUtil#LOWEST_INTEGER
     * @see MatrixUtil#HIGHEST_INTEGER
     * @see MatrixUtil#LOWEST_DOUBLE
     * @see MatrixUtil#HIGHEST_DOUBLE
     */
    public static double search(double[][] matrix, BiPredicate<Number, Number> biPredicate, boolean positiveInfinity) {
        double num = positiveInfinity ? Double.MAX_VALUE : Double.MIN_VALUE;

        for (double[] arr : matrix) {
            for (double s : arr) {
                if (biPredicate.test(s, num)) {
                    num = s;
                }
            }
        }
        return num;
    }

    /**
     * Pokusí se najít v matici číslo na základě podmínek
     *
     * @param matrix       matice, ve které se bude hledat
     * @param biPredicate  podmínka pro číslo
     * @param defaultValue defaultní číslo
     * @param <T>          typ
     * @return číslo na základě podmínek, pokud nenajde, vrátí defaultValue
     * @see MatrixUtil#LOWEST_INTEGER
     * @see MatrixUtil#HIGHEST_INTEGER
     * @see MatrixUtil#LOWEST_DOUBLE
     * @see MatrixUtil#HIGHEST_DOUBLE
     */
    public static <T extends Number> int searchIndex(T[][] matrix, BiPredicate<Number, Number> biPredicate, T defaultValue) {
        int val = defaultValue.intValue();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (biPredicate.test(matrix[i][j], val)) {
                    val = i * matrix.length + j;
                }
            }
        }

        return val;
    }

    /**
     * Pokusí se najít v matici číslo na základě podmínek
     *
     * @param matrix           matice, ve které se bude hledat
     * @param biPredicate      podmínka pro číslo
     * @param positiveInfinity {@code true} znamená, že defaultní číslo bude největší možné a {@code false} znamená nejmenší
     *                         (pro vyhledávání nejměnšího čísla by toto mělo být {@code true} a pro největší číslo {@code false})
     * @return číslo na základě podmínek
     * @see MatrixUtil#LOWEST_INTEGER
     * @see MatrixUtil#HIGHEST_INTEGER
     * @see MatrixUtil#LOWEST_DOUBLE
     * @see MatrixUtil#HIGHEST_DOUBLE
     */
    public static int searchIndex(int[][] matrix, BiPredicate<Number, Number> biPredicate, boolean positiveInfinity) {
        int num = positiveInfinity ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (biPredicate.test(matrix[i][j], num)) {
                    num = i * matrix.length + j;
                }
            }
        }
        return num;
    }

    /**
     * Pokusí se najít v matici číslo na základě podmínek
     *
     * @param matrix           matice, ve které se bude hledat
     * @param biPredicate      podmínka pro číslo
     * @param positiveInfinity {@code true} znamená, že defaultní číslo bude největší možné a {@code false} znamená nejmenší
     *                         (pro vyhledávání nejměnšího čísla by toto mělo být {@code true} a pro největší číslo {@code false})
     * @return číslo na základě podmínek
     * @see MatrixUtil#LOWEST_INTEGER
     * @see MatrixUtil#HIGHEST_INTEGER
     * @see MatrixUtil#LOWEST_DOUBLE
     * @see MatrixUtil#HIGHEST_DOUBLE
     */
    public static int searchIndex(short[][] matrix, BiPredicate<Number, Number> biPredicate, boolean positiveInfinity) {
        int num = positiveInfinity ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (biPredicate.test(matrix[i][j], num)) {
                    num = i * matrix.length + j;
                }
            }
        }
        return num;
    }

    /**
     * Pokusí se najít v matici číslo na základě podmínek
     *
     * @param matrix           matice, ve které se bude hledat
     * @param biPredicate      podmínka pro číslo
     * @param positiveInfinity {@code true} znamená, že defaultní číslo bude největší možné a {@code false} znamená nejmenší
     *                         (pro vyhledávání nejměnšího čísla by toto mělo být {@code true} a pro největší číslo {@code false})
     * @return číslo na základě podmínek
     * @see MatrixUtil#LOWEST_INTEGER
     * @see MatrixUtil#HIGHEST_INTEGER
     * @see MatrixUtil#LOWEST_DOUBLE
     * @see MatrixUtil#HIGHEST_DOUBLE
     */
    public static int searchIndex(double[][] matrix, BiPredicate<Number, Number> biPredicate, boolean positiveInfinity) {
        int num = positiveInfinity ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (biPredicate.test(matrix[i][j], num)) {
                    num = i * matrix.length + j;
                }
            }
        }
        return num;
    }
}
