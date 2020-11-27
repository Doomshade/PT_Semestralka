package git.doomshade.semstralka;

import git.doomshade.semstralka.impl.graph.Storage;
import git.doomshade.semstralka.martin.gui.App;

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

        Main.LOGGER.setLevel(Level.WARNING);
        //String filePath = "C:\\Users\\jakub\\Downloads\\PT\\real_small.txt";
        //Storage storage = read(new File(filePath));

        App app = new App();
        app.startApp();

        //--
        //Simulation matrix = new Simulation(storage);
        //matrix.simulateRestOfDays();

        //System.out.println(Arrays.deepToString(data.getTransportationMatrices()));
        //System.out.println(Arrays.stream(data.optimalPrice).sum());
        //--
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
        readData(itr, pocetTovaren, pocetSupermarketu, pocetDruhuZbozi, pocetDni, cenaPrevozu, pocatecniZasoby, produkceTovaren, poptavkaZbozi);

        if (itr.hasNext()) {
            throw new RuntimeException("Did not read all FFS");
        }


        return new Storage(cenaPrevozu, pocatecniZasoby, produkceTovaren, poptavkaZbozi, pocetTovaren, pocetSupermarketu, pocetDruhuZbozi, pocetDni);
    }

    private static void readData(Iterator<Integer> itr, int pocetTovaren, int pocetSupermarketu, int pocetDruhuZbozi, int pocetDni, int[] cenaPrevozu, int[] pocatecniZasoby, int[] produkceTovaren, int[] poptavkaZbozi) {
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
    }
}