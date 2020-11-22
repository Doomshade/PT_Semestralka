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
public class DataSaver implements IDataSaver {

    private String[] tovarny;
    private String[] supermarkety;
    private String[] zbozi;

    private static final File FOLDER;

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

    @Override
    public File save(IFileData data) throws IOException {
        final int[][][][] production = data.getProduction();

        final int tLen = production[0].length;
        final int sLen = production[0][0].length;
        final int zLen = production[0][0][0].length;
        fillInArrays(tLen, sLen, zLen);

        StringBuilder sb = new StringBuilder("<data>");
        for (int i = 0; i < production.length; i++) {
            sb.append(String.format("<den-%d>", i));
            for (int j = 0; j < tLen; j++) {
                sb.append(String.format("<%s>", tovarny[j]));
                for (int k = 0; k < sLen; k++) {
                    sb.append(String.format("<%s>", supermarkety[j]));
                    for (int l = 0; l < zLen; l++) {
                        sb.append(String.format("<%s>", zbozi[j]));
                        sb.append(production[i][j][k][l]);
                        sb.append(String.format("</%s>", zbozi[j]));
                    }
                    sb.append(String.format("</%s>", supermarkety[j]));
                }
                sb.append(String.format("</%s>", tovarny[j]));
            }
            sb.append(String.format("</den-%d>", i));
        }
        sb.append("</data>");
        final File f = new File("test.xml");
        if (!f.exists()) {
            f.createNewFile();
        }
        try (FileWriter fw = new FileWriter(f)) {
            fw.write(sb.toString());
        }
        return f;
    }
}
