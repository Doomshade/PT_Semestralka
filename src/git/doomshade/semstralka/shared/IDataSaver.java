package git.doomshade.semstralka.shared;

import java.io.File;
import java.io.IOException;

/**
 * @author Jakub Šmrha
 * @version 1.0 (22.11.2020)
 */
public interface IDataSaver {

    /**
     * Uloží data do čitelného formátu
     *
     * @param data data, která se mají uložit
     * @return vytvořenou <b>SLOŽKU</b>, uvnitř kterých jsou soubory s daty
     * @throws IOException pokud se nepodaří uložit data
     */
    File save(IFileData data) throws IOException;
}
