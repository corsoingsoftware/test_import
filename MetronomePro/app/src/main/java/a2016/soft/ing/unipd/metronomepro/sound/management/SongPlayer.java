package a2016.soft.ing.unipd.metronomepro.sound.management;

import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Federico Favotto on 08/12/2016.
 */


public interface SongPlayer {



    /**
     * Start the current song
     */
    void play();

//deleted initialized methods by Federico, no more necessary


    /**
     * Pause the current song
     */
    void pause();

    /**
     * Stop the current song
     */
    void stop();

    /**
     * load the song into the current playlist.
     * @param song
     */
    void load(Song song);

    /**
     * Return the song
     * @param s song to search
     * @return the bytearray to describe song in current format
     */
    byte[] getSong(Song s);

    /**
     * State of current song
     * @return PLaystate that describes state
     */
    PlayState getState();

    /**
     * Write the songs in the AudioTrack's buffer
     * @param songs
     */
    void write(Song[] songs);


    interface SongPlayerCallback {
        /**
         * method called when play ends
         */
        void playEnded(SongPlayer origin);
    }
}
