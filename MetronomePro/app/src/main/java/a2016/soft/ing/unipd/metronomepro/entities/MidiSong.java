package a2016.soft.ing.unipd.metronomepro.entities;

/**
 * Created by feder, mune, alberto on 22/12/2016.
 */

/**
 * Serve dichiarare un metodo per la modifica del percorso dentro a MidiSong?
 */

public interface MidiSong extends Song {

    /**
     * return the path where the file is stored
     * @return the path in String
     */
    String path();

    /**
     * return the duration of the file
     * @return duration of the song in milliseconds
     */
    int duration();

    /**
     * return the bpm of the song
     * @return the value of the bpm of the song
     */
    int getBpm();

    /**
     * return the type of song
     * @return the file exstension in String
     */
    String getType();

}
