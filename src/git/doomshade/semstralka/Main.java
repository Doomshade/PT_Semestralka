package git.doomshade.semstralka;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static final Pattern POCET_PATTERN = Pattern.compile("([\\d]+) ([\\d]+) ([\\d]+) ([\\d]+)");

    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\Kuba\\Desktop\\skull\\PT";
        String fileName = "real_large.txt";

        long time = System.currentTimeMillis();
        final Data data = read(new File(path, fileName));

        if (data != null) {
            System.out.println("Reading " + fileName + " took " + ((System.currentTimeMillis() - time) / 1000d) + "s to read");
            System.out.println(data.getProdukce(1, 100, 2));
        }
    }

    private static Data read(File file) throws IOException {
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
        }

        // reassign the scanner
        sc = new Scanner(tempFile);
        final String blokPocet = sc.nextLine();
        final Matcher m = POCET_PATTERN.matcher(blokPocet);

        // wrong header
        if (!m.find()) return null;

        short d = Short.parseShort(m.group(1));
        short s = Short.parseShort(m.group(2));
        short z = Short.parseShort(m.group(3));
        short t = Short.parseShort(m.group(4));


        short[] cenaPrevozu = new short[d * s];
        short[] pocatecniZasoby = new short[z * s];
        short[] produkceTovaren = new short[d * z * t];
        short[] poptavkaZbozi = new short[s * z * t];

        for (int i = 0; i < d * s; i++) {
            cenaPrevozu[i] = sc.nextShort();

        }

        for (int i = 0; i < z * s; i++) {
            pocatecniZasoby[i] = sc.nextShort();
        }

        for (int i = 0; i < d * z * t; i++) {
            produkceTovaren[i] = sc.nextShort();

        }

        for (int i = 0; i < s * z * t; i++) {
            poptavkaZbozi[i] = sc.nextShort();

        }

        return new Data(cenaPrevozu, pocatecniZasoby, produkceTovaren, poptavkaZbozi, d, s, z, t);
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
