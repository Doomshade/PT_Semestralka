package test.git.doomshade.semestralka;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import test.git.doomshade.semestralka.martin.MartinTest;
import test.git.doomshade.semestralka.smrha.SmrhaTest;

/**
 * @author Jakub Šmrha
 * @version 1.0 (27.10.2020)
 */
public class TestRunner {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(SmrhaTest.class, MartinTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getTrace());
        }
    }
}
