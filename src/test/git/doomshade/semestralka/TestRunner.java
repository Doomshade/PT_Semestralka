package test.git.doomshade.semestralka;

import git.doomshade.semstralka.Main;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import test.git.doomshade.semestralka.martin.MartinTests;
import test.git.doomshade.semestralka.smrha.SmrhaTests;

import java.util.logging.Level;

/**
 * @author Jakub Šmrha
 * @version 1.0 (27.10.2020)
 */
public class TestRunner {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(SmrhaTests.class, MartinTests.class);

        for (Failure failure : result.getFailures()) {
            Main.LOGGER.severe(failure.getTrace());
        }
    }
}
