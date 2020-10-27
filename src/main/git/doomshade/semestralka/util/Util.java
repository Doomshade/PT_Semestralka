package main.git.doomshade.semestralka.util;

/**
 * @author Jakub Šmrha
 * @version 1.0 (27.10.2020)
 */
public class Util {

    public static long benchmark(Runnable action){
        long l = System.nanoTime();
        action.run();
        return System.nanoTime() - l;
    }
}
