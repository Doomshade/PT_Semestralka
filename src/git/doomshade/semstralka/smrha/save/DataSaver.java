package git.doomshade.semstralka.smrha.save;

import git.doomshade.semstralka.shared.IDataSaver;
import git.doomshade.semstralka.shared.IFileData;
import git.doomshade.semstralka.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Třída pro uložení dat do textové formy
 *
 * @author Jakub Šmrha
 * @version 1.0 (22.11.2020)
 */
public class DataSaver implements IDataSaver {

    /**
     * Pole jmen továren
     */
    private static String[] tovarny;

    /**
     * Pole jmen supermarketů
     */
    private static String[] supermarkety;

    /**
     * Pole jmen zboží
     */
    private static String[] zbozi;

    /**
     * Adresář pro pole jmen souborů
     */
    private static final File NAMES_FOLDER;

    /**
     * Adresář pro výstup
     */
    private static final File DATA_FOLDER;

    static {
        // nastavíme vytvoříme adresář
        File PATH = new File("resources");
        NAMES_FOLDER = new File(PATH, "names");
        DATA_FOLDER = new File(NAMES_FOLDER, "data");
        DATA_FOLDER.mkdirs();
    }

    /**
     * Vytvoří defaultní soubory
     *
     * @throws IOException pokud nastane chyba ve vytvoření souboru
     */
    private void createDefaultNames() throws IOException {
        writeToFile("tovarny.txt", new String[]{});
        writeToFile("supermarkety.txt", new String[]{});
        writeToFile("zbozi.txt", new String[]{});
    }

    /**
     * Zapíše data do souboru
     *
     * @param fileName jméno souboru
     * @param data     data
     * @throws IOException pokud nastane chyba při vytvoření souboru
     */
    private void writeToFile(String fileName, String[] data) throws IOException {
        File file = getNamesFile(fileName);
        try (PrintWriter pw = new PrintWriter(file)) {
            for (String s : data) {
                pw.println(s);
            }
        }
    }

    /**
     * Vrátí soubor v names adresáři a případně ho vytvoří
     *
     * @param fileName jméno souboru
     * @return soubor v names adresáři
     * @throws IOException pokud nelze vytvořit soubor
     */
    private File getNamesFile(String fileName) throws IOException {

        // cmon do I rly need to comment this :D
        File file = new File(NAMES_FOLDER, fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }

    /**
     * Doplní do polí jmen příslušná jména
     *
     * @param tLen počet továren
     * @param sLen počet supermarketů
     * @param zLen počet zboží
     * @throws IOException pokuid nastane chyba při vytváření souboru
     */
    private void fillNamesArrays(int tLen, int sLen, int zLen) throws IOException {

        // již inicializováno
        if (tovarny != null) return;

        // +1 kvůli hlavičce v poli
        tovarny = new String[tLen + 1];
        supermarkety = new String[sLen + 1];
        zbozi = new String[zLen + 1];

        // adresář se jmény neexistuje, vytvoří default
        if (!NAMES_FOLDER.exists()) {
            createDefaultNames();
        }

        // doplní do polí jména
        arrayFill(tovarny, "tovarny");
        arrayFill(supermarkety, "supermarkety");
        arrayFill(zbozi, "zbozi");
    }

    /**
     * Doplní do pole jména
     *
     * @param arr  pole, do kterého se to má doplnit
     * @param name jméno souboru (supermarkety/tovarny/zbozi)
     * @throws FileNotFoundException pokud neexistuje soubor se jménem "%name%.txt" ve složce names
     */
    private void arrayFill(String[] arr, String name) throws FileNotFoundException {

        // names/tovarny.txt
        File file = new File(NAMES_FOLDER, name.concat(".txt"));

        // list pro čtení, zde je arraylist preferred over linkedlist kvůli get() ve forloopu later on
        List<String> read = new ArrayList<>();

        // začneme číst ze souboru
        try (Scanner sc = new Scanner(file)) {

            // uděláme si list (list kvůli shuffle, vysvětleno později)
            List<Integer> availableIndexes = new ArrayList<>();

            // poslední available index === počet lines v souboru
            int i = 0;
            while (sc.hasNextLine()) {
                availableIndexes.add(i++);
                read.add(sc.nextLine().replaceAll(" ", ""));
            }

            // zrandomizujeme tento list, ať to není pořád to samý :)
            Collections.shuffle(availableIndexes);

            // nastavíme hlavičku pole na name (tovarny/supermarkety/zbozi)
            arr[0] = name;

            // doplníme do pole jména
            for (int j = 0; j < arr.length - 1 && j < availableIndexes.size(); j++) {
                final String s = read.get(availableIndexes.get((j)));
                arr[j + 1] = s;
            }

            // pokud jmen nebylo dostatek, doplníme s name-index (supermarkety-3 např.)
            for (int j = i + 1; j < arr.length; j++) {
                arr[j] = name + "-" + j;
            }
        }
    }

    /**
     * Vytvoří StringBuilder a zavolá {@link DataSaver#fillStringBuilder(Object, StringBuilder, int, String[]...)} s příslušnými parametry
     *
     * @param matrix matice/pole, ze které se má vytvořit stringbuilder
     * @param names  pole jmen v příslušném pořadí (např. pro production to musí být pole v tomto pořadí: dny, továrny, supermarkety, zboží)
     * @return matici v XML formátu uloženou ve StringBuilderu
     * @see IFileData#getProduction()
     */
    private static StringBuilder getStringBuilder(Object matrix, String[]... names) {
        StringBuilder sb = new StringBuilder();
        fillStringBuilder(matrix, sb, 0, names);
        return sb;
    }

    /**
     * Doplní rekurzivně do stringbuilderu data v XML formátu o matici.
     * Např. production (4D pole) -><br> {@code <dny><tovarny><supermarkety><zbozi>VALUE</zbozi></supermarkety></tovarny></dny>}
     *
     * @param array <b>int</b> pole/matice/data - zde je důležité, že je to matice/pole intů, value předpokládaná non-floating, přetypována na long
     * @param sb    current stringbuilder
     * @param index current index v names
     * @param names pole jmen
     */
    private static void fillStringBuilder(Object array, StringBuilder sb, int index, String[]... names) {

        // seznam current jmen
        final String[] currNames = names[index];
        try {

            // pokusíme se přetypovat na matici, pokud to není matice, hodí se classcastexception
            Object[] arr = (Object[]) array;

            // (1) <dny>  ---- doplníme header tag v poli
            sb.append(String.format("<%s>", currNames[0]));
            for (int i = 0; i < currNames.length - 1; i++) {

                // doplníme rekurzivně další jména
                final String currName = currNames[i + 1];

                // 1. iterace: 2->3->4; 2. iterace 5->6->7; ...
                // (2) <dny><den-0>
                // (5)<dny><den-0>...</den-0><den-1>
                sb.append(String.format("<%s>", currName));

                // (3) <dny><den-0><tovarny>....</tovarny>
                // (6)<dny><den-0>...</den-0><den-1><tovarny>....</tovarny>
                fillStringBuilder(arr[i], sb, index + 1, names);

                // (4) <dny><den-0><tovarny>....</tovarny></den-0>
                // (7)<dny><den-0>...</den-0><den-1><tovarny>....</tovarny></den-1>
                sb.append(String.format("</%s>", currName));
            }

            // (8) <dny>...</dny>
            sb.append(String.format("</%s>", currNames[0]));
        }
        // není to matice -> je to pole nebo single data
        catch (ClassCastException e) {
            try {
                int[] arr = (int[]) array;
                // nyní víme, že jsme v 1d poli a je to pouze pole intů (jiný parametr nelze vložit až na single long)

                // doplníme hlavičku (pro produkci např. <zbozi>)
                sb.append(String.format("<%s>", currNames[0]));

                // procyklujeme se polem a doplníme hodnotu z pole (zde je jisté, že je přítomna hodnota a ne další pole/matice)
                for (int i = 0; i < currNames.length - 1; i++) {

                    // nulové zboží vyfiltrujeme
                    if (arr[i] != 0) {

                        // doplníme value (např. <Adidas>150</Adidas>)
                        final String currName = currNames[i + 1];
                        sb.append(String.format("<%s>%d</%s>", currName, arr[i], currName));
                    }
                }

                // doplníme konec hlavicky (pro produkci např. </zbozi>)
                sb.append(String.format("</%s>", currNames[0]));
            }
            // není to pole -> je to single data, single data nemá hlavičku
            catch (ClassCastException exception) {
                Number data = (Number) array;
                String currName = currNames[0];

                sb.append(String.format("<%s>%d</%s>", currName, data.longValue(), currName));
            }
        }
    }

    /**
     * Vrátí pole dnů s hlavičkou na základě velikosti pole
     *
     * @param arrayLen délka pole
     * @return pole dnů s hlavičkou na základě velikosti pole
     */
    private static String[] getDny(int arrayLen) {
        String[] arr = new String[arrayLen + 1];

        // hlavička
        arr[0] = "dny";
        for (int i = 1; i < arr.length; i++) {

            // den-0, den-1, ...
            arr[i] = "den-" + (i - 1);
        }
        return arr;
    }

    /**
     * Vytvoří soubor v data adresáři a zapíše do něj data ze stringbuilderu
     *
     * @param data     data ze stringbuilderu v xml formátu
     * @param fileName jméno souboru
     * @return vytvořený soubor s daty
     * @throws IOException pokud se nepodařilo vytvořit soubor
     */
    private File createAndWriteData(StringBuilder data, String fileName) throws IOException {

        // vytvoříme soubor
        File file = new File(DATA_FOLDER, fileName + ".xml");
        if (!file.exists()) {
            file.createNewFile();
        }

        // zapíšeme do něj hlavičku XML formátu
        // TODO přidat css?
        try (PrintWriter fw = new PrintWriter(file)) {
            fw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            fw.print(data.toString());
        }
        return file;
    }

    @Override
    public File save(IFileData data) throws IOException {

        // getneme si všechno možné na začátku
        final int[][][][] production = data.getProduction();
        final int[][][] overProduction = data.getOverProduction();
        final long simulationLength = data.getSimulationLength();
        final int[][][] supermarketOverview = data.getSupermarketOverview();
        final Pair<Integer, Integer[][]> noSolution = data.noSolutionData();

        // prvně doplníme defaultní jména
        // továrny
        final int xLen = production[0].length;

        // supermarkety
        final int yLen = production[0][0].length;

        // zboží
        final int zLen = production[0][0][0].length;
        fillNamesArrays(xLen, yLen, zLen);

        // uděláme si pole o dnech (den-0, den-1, den-2, ...)
        final String[] dny = getDny(production.length);

        // následně začneme zapisovat do souborů

        File[] files = new File[]{
                createAndWriteData(getStringBuilder(production, dny, tovarny, supermarkety, zbozi), "production"),
                createAndWriteData(getStringBuilder(overProduction, dny, tovarny, zbozi), "over-production"),
                createAndWriteData(getStringBuilder(simulationLength, new String[]{"délka-simulace"}), "simulation-length"),
                createAndWriteData(getStringBuilder(supermarketOverview, dny, supermarkety, zbozi), "supermarket-overview")
        };
        return DATA_FOLDER;
    }

}
