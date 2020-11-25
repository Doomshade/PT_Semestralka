package git.doomshade.semstralka.shared;

import git.doomshade.semstralka.smrha.save.DataSaver;

/**
 * Třída, ve které se nachází metody pro vytvoření instancí shared rozhraní
 *
 * @author Jakub Šmrha
 * @version 1.0 (24.11.2020)
 * @see IDataSaver
 * @see IFileData
 */
public final class SharedManager {

    /**
     * nepotřebujeme dělat instanci této třídy
     */
    private SharedManager() {
    }

    /**
     * @return novou instanci data saveru
     */
    public static IDataSaver getDataSaver() {
        return new DataSaver();
    }

    /**
     * @return novou instanci dat
     */
    public static IFileData getFileData() {
        return null;
    }
}
