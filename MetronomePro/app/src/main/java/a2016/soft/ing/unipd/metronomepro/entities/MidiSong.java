package a2016.soft.ing.unipd.metronomepro.entities;

/**
 * Created by Munerato, Moretto on 22/12/2016.
 */

public interface MidiSong extends Song {

    /**
     * return the path where the file is stored
     * @return the path in String
     */
    String getPath();

    /**
     * Sets the path of midi song
     * @param newPath
     */
    void setPath(String newPath);

}
