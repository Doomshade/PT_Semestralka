package git.doomshade.semstralka;

import git.doomshade.semstralka.impl.graph.Storage;
import git.doomshade.semstralka.martin.DayData;
import git.doomshade.semstralka.martin.Simulation;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;
import java.util.stream.Collectors;

/**
 * Spouštěcí třída
 */
public class Main {

    /**
     * Logger této aplikace
     */
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tT] [%2$s] %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                        new Date(lr.getMillis()),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage()
                );
            }
        });
        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(handler);
    }

    /**
     * Main...
     *
     * @param args
     * @throws IOException pokud nastane chyba při čtení souboru
     */
    public static void main(String[] args) throws IOException {
        /*
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

        String filePath = "C:\\Users\\Doomshade\\Desktop\\skull\\PT\\semestralka\\tests\\test_price.txt";
        Storage storage = read(new File(filePath));

        Simulation matrix = new Simulation(storage);
        DayData data = matrix.simulateNextDay();
        data.getTransportationMatrices();

        System.out.println(Arrays.deepToString(data.getTransportationMatrices()));
        System.out.println(Arrays.stream(data.optimalPrice).sum());
        //
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
        time = System.currentTimeMillis() - time;

        LOGGER.info("Reading " + fileName + " took " + (time / 1000d) + "s to read");
        return data;
    }

    public static Storage read(File file) throws IOException {

        LOGGER.fine(file.getName());
        long l = System.currentTimeMillis();

        // allright idk, co tu vybrat za kolekci, v testech obě performují hodně podobně
        // dáváme pouze add, tak linked
        Collection<Integer> c = new LinkedList<>();

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                final String s = sc.nextLine();

                // vyfiltrujeme prázdné řetězce a komenty
                if (!s.isEmpty() && s.charAt(0) != '#') {

                    // splitne "s" podle mezer, zbaví se prázdných stringů (stává se somehow), namapuje na int a collectne do listu
                    c.addAll(
                            Arrays.stream(s.split(" "))
                                    .filter(x -> !x.isEmpty())
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toCollection(LinkedList::new)));
                }
            }

        }

        l = System.currentTimeMillis() - l;
        // log
        LOGGER.fine("Read took " + l + "ms");

        final Iterator<Integer> itr = c.iterator();
        /*
        final String blokPocet = iterator.next();

        // pattern byl puvodne pro pocetTovaren apod.
        final Pattern pocetPattern = Pattern.compile("([\\d]+) ([\\d]+) ([\\d]+) ([\\d]+)");
        final Matcher m = pocetPattern.matcher(blokPocet);

        // špatná hlavička - chybí počty továren, supermarketů, ...
        if (!m.find()) {
            throw new IOException("Soubor nesplňuje požadovaný formát");
        }*/

        // data se dají přečíst, pokračujeme
        // TEST pro martina
        int pocetTovaren = itr.next();
        int pocetSupermarketu = itr.next();
        int pocetDruhuZbozi = itr.next();
        int pocetDni = itr.next();

        // nainicializujeme do pole
        // později si to převedeme do lepších struktur
        int[] cenaPrevozu = new int[pocetTovaren * pocetSupermarketu];
        int[] pocatecniZasoby = new int[pocetDruhuZbozi * pocetSupermarketu];
        int[] produkceTovaren = new int[pocetTovaren * pocetDruhuZbozi * pocetDni];
        int[] poptavkaZbozi = new int[pocetSupermarketu * pocetDruhuZbozi * pocetDni];

        // teď přečteme celý soubor
        // scanner sám hodí exception, pokud bude špatný formát (vyskytne se string nebo špatný počet dat)
        // TODO předělat do listu
        for (int i = 0; i < pocetTovaren * pocetSupermarketu; i++) {
            cenaPrevozu[i] = itr.next();

        }

        for (int i = 0; i < pocetDruhuZbozi * pocetSupermarketu; i++) {
            pocatecniZasoby[i] = itr.next();
        }

        for (int i = 0; i < pocetTovaren * pocetDruhuZbozi * pocetDni; i++) {
            produkceTovaren[i] = itr.next();
        }

        for (int i = 0; i < pocetSupermarketu * pocetDruhuZbozi * pocetDni; i++) {
            poptavkaZbozi[i] = itr.next();
        }

        if (itr.hasNext()) {
            throw new RuntimeException("Did not read all FFS");
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