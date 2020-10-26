package git.doomshade.semstralka;

import git.doomshade.semstralka.impl.graph.Graph;
import git.doomshade.semstralka.impl.graph.Storage;
import git.doomshade.semstralka.impl.graph.matrix.IncidentialMatrixGraph;
import git.doomshade.semstralka.impl.graph.matrix.NeighbouringMatrixGraph;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Spouštěcí třída
 */
public class Main {

    /**
     * Logger této aplikace
     */
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Main...
     *
     * @param args
     * @throws IOException pokud nastane chyba při čtení souboru
     */
    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\Doomshade\\Desktop\\skull\\PT\\semestralka\\tests";
        String fileName = "real_large.txt";
        //testRead(path, fileName);
        /*testGraph(1, 20, graph -> {
            graph.addEdge(new Edge(0, 5, -1));
            graph.addEdge(new Edge(0, 15, 8));
            graph.addEdge(new Edge(0, 2, 12));
            graph.addEdge(new Edge(0, 3, 15));
            graph.addEdge(new Edge(0, 4, 11));


            graph.addEdge(new Edge(3, 1, 4));
            graph.addEdge(new Edge(3, 2, 52));
            graph.addEdge(new Edge(3, 4, 47));

            System.out.println(graph.neighbours(0));

            if (graph instanceof MatrixGraph) {

                // chceme printnout celý graf
                ((MatrixGraph) graph).print(System.out, false);
            } else {
                graph.print(System.out);
            }
        });*/
    }

    /**
     * Testovací metoda pro čtení ze souboru
     *
     * @param path     cesta k souboru
     * @param fileName jméno souboru
     * @return data, které se přečetla ze souboru
     * @throws IOException pokud nelze přečíst soubor
     */
    private static Storage testRead(String path, String fileName) throws IOException {
        long time = System.currentTimeMillis();
        final Storage data = read(new File(path, fileName));
        time = (System.currentTimeMillis() - time);

        LOGGER.info("Reading " + fileName + " took " + (time / 1000d) + "s to read");
        return data;
    }

    /**
     * Testovací metoda pro implementace grafů
     *
     * @param graphType <ul>
     *                  <li>1 = Orientovaný {@link NeighbouringMatrixGraph}</li>
     *                  <li>2 = Orientovaný {@link IncidentialMatrixGraph}</li>
     *                  <li>4 = Neorientovaný {@link NeighbouringMatrixGraph}</li>
     *                  <li>5 = Neorientovaný {@link IncidentialMatrixGraph}</li>
     *                  </ul>
     * @param size      velikost grafu
     * @param consumer  co provést za akci s inicializovaným grafem
     */
    private static void testGraph(int graphType, int size, Consumer<Graph> consumer) {
        final Graph graph;

        switch (graphType) {
            case 5:
                graph = new IncidentialMatrixGraph(false);
                break;
            case 4:
                graph = new NeighbouringMatrixGraph(false);
                break;
            case 2:
                graph = new IncidentialMatrixGraph(true);
                break;
            case 1:
                graph = new NeighbouringMatrixGraph(true);
                break;
            case 0:
                //graph = new LinkedListGraph(true);
            default:
                throw new RuntimeException("Invalidní typ grafu");
        }

        graph.initializeGraph(size);

        consumer.accept(graph);
    }

    public static Storage read(File file) throws IOException {
        int index = 0;

        Scanner sc = new Scanner(file);

        // vytvoříme temp file s vyfiltrovanými komenty
        List<String> list = new ArrayList<>();
        final File tempFile = Files.createTempFile("temp", "txt").toFile();

        try (final PrintWriter out = new PrintWriter(tempFile)) {
            while (sc.hasNextLine()) {
                final String s = sc.nextLine();

                // vyfiltrujeme prázdné řetězce a komenty
                if (!s.isEmpty() && !s.startsWith("#")) {
                    out.println(s);
                    list.add(s);
                }
            }

            // ukončíme čtení ze souboru
            sc.close();
        }

        // TODO konvertovat do linkedlistu (nebo nějaký queue) a pak číst tam odsud
        // nastavíme scanner na temp file a čteme
        sc = new Scanner(tempFile);
        final String blokPocet = list.get(index++);

        // pattern byl puvodne pro pocetTovaren apod.
        final Pattern POCET_PATTERN = Pattern.compile("([\\d]+) ([\\d]+) ([\\d]+) ([\\d]+)");
        final Matcher m = POCET_PATTERN.matcher(blokPocet);

        // špatná hlavička - chybí počty továren, supermarketů, ...
        if (!m.find()) {
            throw new IOException("Soubor nesplňuje požadovaný formát");
        }

        // data se dají přečíst, pokračujeme
        short pocetTovaren = sc.nextShort();
        short pocetSupermarketu = sc.nextShort();
        short pocetDruhuZbozi = sc.nextShort();
        short pocetDni = sc.nextShort();


        // nainicializujeme do pole
        // později si to převedeme do lepších struktur
        short[] cenaPrevozu = new short[pocetTovaren * pocetSupermarketu];
        short[] pocatecniZasoby = new short[pocetDruhuZbozi * pocetSupermarketu];
        short[] produkceTovaren = new short[pocetTovaren * pocetDruhuZbozi * pocetDni];
        short[] poptavkaZbozi = new short[pocetSupermarketu * pocetDruhuZbozi * pocetDni];

        // teď přečteme celý soubor
        // scanner sám hodí exception, pokud bude špatný formát (vyskytne se string nebo špatný počet dat)
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
