package git.doomshade.semstralka.smrha.save;

import git.doomshade.semstralka.shared.IDataSaver;
import git.doomshade.semstralka.shared.IFileData;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Jakub Šmrha
 * @version 1.0 (22.11.2020)
 */
public class FileManager {

    public static IDataSaver getDataSaver() {
        return new DataSaver();
    }

    private static String[] getDny(Object array) {
        final int length = ((Object[]) array).length;
        String[] ret = new String[length];
        for (int i = 0; i < length; i++) {
            ret[i] = "den-" + i;
        }
        return ret;
    }

    public static void test(IFileData data) {
        final DataSaver dataSaver = (DataSaver) getDataSaver();
        try {
            dataSaver.save(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final int[][][][] production = data.getProduction();
        System.out.println(Arrays.deepToString(production));

        StringBuilder sb = start();
        fillStringBuilder(production, sb, 0, getDny(production), DataSaver.tovarny, DataSaver.supermarkety, DataSaver.zbozi);
        System.out.println(end(sb).toString());
    }

    private static StringBuilder start() {
        return new StringBuilder("<data>");
    }

    private static StringBuilder end(StringBuilder sb) {
        return sb.append("</data>");
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
                System.out.println(arr[i]);
                sb.append(String.format("<%s>%d</%s>", shit[i], arr[i], shit[i]));
            }
        }
    }
}
