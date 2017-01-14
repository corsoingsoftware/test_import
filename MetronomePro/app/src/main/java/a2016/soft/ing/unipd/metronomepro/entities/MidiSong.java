package a2016.soft.ing.unipd.metronomepro.entities;

/**
 * Created by mune, alberto on 22/12/2016.
 */

/**
 * Serve dichiarare un metodo per la modifica del percorso dentro a MidiSong? Sì
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

    /**
     * return the duration of the file
     * @return duration of the song in milliseconds
     */
    int getDuration();

    /**
     * Sets the duration of the song in milliseconds
     */
    void setDuration(int millisDuration);
    //Next lines commented by Federico, getBPM can't implemented ATM, and getType isn't necessary, we can get the type of song by the context of it
//    /**
//     * return the bpm of the song
//     * @return the value of the bpm of the song
//    int getBpm();*/

//    /**
//     * return the type of song
//     * @return the file exstension in String
//     */
//    String getType();

}
