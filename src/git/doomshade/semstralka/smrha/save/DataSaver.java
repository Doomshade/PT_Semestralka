package git.doomshade.semstralka.smrha.save;

import git.doomshade.semstralka.shared.IDataSaver;
import git.doomshade.semstralka.shared.IFileData;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author Jakub Šmrha
 * @version 1.0 (22.11.2020)
 */
class DataSaver implements IDataSaver {

    static String[] tovarny;
    static String[] supermarkety;
    static String[] zbozi;

    private static final File FOLDER;

    DataSaver() {
    }


    static {
        File PATH = new File("resources");
        FOLDER = new File(PATH, "names");
        FOLDER.mkdirs();
    }

    private void createDefaultNames() throws IOException {
        writeToFile("tovarny.txt", new String[]{});
        writeToFile("supermarkety.txt", new String[]{});
        writeToFile("zbozi.txt", new String[]{});
    }

    private void writeToFile(String fileName, String[] data) throws IOException {
        File file = getFile(fileName);
        try (PrintWriter pw = new PrintWriter(file)) {
            for (String s : data) {
                pw.println(s);
            }
        }
    }

    private File getFile(String fileName) throws IOException {
        File file = new File(FOLDER, fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }

    private void fillInArrays(int tLen, int sLen, int zLen) throws IOException {
        if (tovarny != null) return;
        tovarny = new String[tLen];
        supermarkety = new String[sLen];
        zbozi = new String[zLen];

        if (!FOLDER.exists()) {
            createDefaultNames();
        }

        arrayFill(tovarny, "tovarny");
        arrayFill(supermarkety, "supermarkety");
        arrayFill(zbozi, "zbozi");
    }

    private void arrayFill(String[] arr, String name) throws FileNotFoundException {
        File file = new File(FOLDER, name.concat(".txt"));
        System.out.println(file.getAbsolutePath());
        try (Scanner sc = new Scanner(file)) {
            int i = 0;
            while (i < arr.length && sc.hasNextLine()) {
                arr[i++] = sc.nextLine().replaceAll(" ", "");
            }
            if (i != arr.length - 1) {
                for (int j = i; j < arr.length; j++) {
                    arr[j] = name + "-" + j;
                }
            }
        }
        Collections.shuffle(Arrays.stream(arr).collect(Collectors.toList()));
    }

    private static StringBuilder getStringBuilder(Object array, String[]... stuff) {
        StringBuilder sb = new StringBuilder("<data>");
        fillStringBuilder(array, sb, 0, stuff);
        sb.append("</data>");
        return sb;
    }

    private static void fillStringBuilder(Object array, StringBuilder sb, int index, String[]... stuff) {
        try {
            Object[] arr = (Object[]) array;
            for (int i = 0; i < arr.length; i++) {
                sb.append(String.format("<%s>", stuff[index][i]));
                fillStringBuilder(arr[i], sb, index + 1, stuff);
                sb.append(String.format("</%s>", stuff[index][i]));
            }
        } catch (ClassCastException e) {
            int[] arr = (int[]) array;
            String[] shit = stuff[index];

            for (int i = 0; i < arr.length; i++) {
                sb.append(String.format("<%s>%d</%s>", shit[i], arr[i], shit[i]));
            }
        }
    }

    private StringBuilder getProduction(int[][][][] production) {
        int zLen = production[0][0][0].length;
        int sLen = production[0][0].length;
        int tLen = production[0].length;

        // root
        StringBuilder sb = new StringBuilder("<data>");
        for (int i = 0; i < production.length; i++) {

            // index dne
            sb.append(String.format("<den-%d>", i));
            for (int j = 0; j < tLen; j++) {

                // tovarny
                sb.append(String.format("<%s>", tovarny[j]));
                for (int k = 0; k < sLen; k++) {

                    // supermarkety
                    sb.append(String.format("<%s>", supermarkety[k]));
                    for (int l = 0; l < zLen; l++) {

                        // zbozi
                        sb.append(String.format("<%s>", zbozi[l]));
                        final int aa = production[i][j][k][l];
                        sb.append(aa);
                        sb.append(String.format("</%s>", zbozi[l]));
                    }
                    sb.append(String.format("</%s>", supermarkety[k]));
                }
                sb.append(String.format("</%s>", tovarny[j]));
            }
            sb.append(String.format("</den-%d>", i));
        }
        sb.append("</data>");
        return sb;
    }

    @Override
    public File save(IFileData data) throws IOException {
        final int[][][][] production = data.getProduction();
        final int tLen = production[0].length;
        final int sLen = production[0][0].length;
        final int zLen = production[0][0][0].length;
        fillInArrays(tLen, sLen, zLen);

        StringBuilder sb = getProduction(production);

        final File f = new File("production.xml");
        if (!f.exists()) {
            f.createNewFile();
        }
        try (FileWriter fw = new FileWriter(f)) {
            fw.write(sb.toString());
        }

        return f;
    }

}
