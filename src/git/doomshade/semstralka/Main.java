package git.doomshade.semstralka;

import git.doomshade.semstralka.graph.MatrixGraph;
import git.doomshade.semstralka.graph.Storage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Spousteci trida
 */
public class Main {

    private static final Pattern POCET_PATTERN = Pattern.compile("([\\d]+) ([\\d]+) ([\\d]+) ([\\d]+)");

    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\Kuba\\Desktop\\skull\\PT";
        String fileName = "real_large.txt";

        long time = System.currentTimeMillis();
        final Storage data = read(new File(path, fileName));

        if (data != null) {
            System.out.println("Reading " + fileName + " took " + ((System.currentTimeMillis() - time) / 1000d) + "s to read");
            new MatrixGraph(data);
        }
    }

    private static Storage read(File file) throws IOException {
        Scanner sc = new Scanner(file);

        // create a temp file with comments filtered out
        final File tempFile = Files.createTempFile("temp", "txt").toFile();

        try (final PrintWriter out = new PrintWriter(tempFile)) {
            while (sc.hasNextLine()) {
                final String s = sc.nextLine();

                // filter out empty strings and comments first
                if (!s.isEmpty() && !s.startsWith("#")) {
                    out.println(s);
                }
            }

            // we stop reading from the file
            sc.close();
        }

        // reassign the scanner
        sc = new Scanner(tempFile);
        final String blokPocet = sc.nextLine();
        final Matcher m = POCET_PATTERN.matcher(blokPocet);

        // wrong header, return null
        if (!m.find()) return null;

        // the data is likely readable, continue
        short pocetTovaren = Short.parseShort(m.group(1));
        short pocetSupermarketu = Short.parseShort(m.group(2));
        short pocetDruhuZbozi = Short.parseShort(m.group(3));
        short pocetDni = Short.parseShort(m.group(4));


        // initialize one dimensional arrays
        // we will convert these into matrixes later on
        short[] cenaPrevozu = new short[pocetTovaren * pocetSupermarketu];
        short[] pocatecniZasoby = new short[pocetDruhuZbozi * pocetSupermarketu];
        short[] produkceTovaren = new short[pocetTovaren * pocetDruhuZbozi * pocetDni];
        short[] poptavkaZbozi = new short[pocetSupermarketu * pocetDruhuZbozi * pocetDni];

        // pretty straight forward reading to an array
        for (int i = 0; i < pocetTovaren * pocetSupermarketu; i++) {
            cenaPrevozu[i] = sc.nextShort();

        }

        for (int i = 0; i < pocetDruhuZbozi * pocetSupermarketu; i++) {
            pocatecniZasoby[i] = sc.nextShort();
        }

        for (int i = 0; i < pocetTovaren * pocetDruhuZbozi * pocetDni; i++) {
            produkceTovaren[i] = sc.nextShort();

        }

        for (int i = 0; i < pocetSupermarketu * pocetDruhuZbozi * pocetDni; i++) {
            poptavkaZbozi[i] = sc.nextShort();

        }

        return new Storage(cenaPrevozu, pocatecniZasoby, produkceTovaren, poptavkaZbozi, pocetTovaren, pocetSupermarketu, pocetDruhuZbozi, pocetDni);
    }

    /*
    // this ones correct
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < s; j++) {
                cenaPrevozu[i][j] = sc.nextInt();
            }
        }

        // this ones correct
        for (int i = 0; i < z; i++) {
            for (int j = 0; j < s; j++) {
                pocatecniZasoby[i][j] = sc.nextInt();
            }
        }

        // TODO
        // this ones wrong
        for (int i = 0; i < d * s * t; i++) {
            for (int j = 0; j < d; j++) {
                produkceTovaren[i][j] = sc.nextInt();

            }
        }

        // this ones wrong
        for (int i = 0; i < d * s * t; i++) {
            for (int j = 0; j < s; j++) {
                poptavkaZbozi[i][j] = sc.nextInt();

            }
        }
     */
}
