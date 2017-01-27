package a2016.soft.ing.unipd.metronomepro.entities;

import android.os.Parcelable;

import java.util.List;

/**
 * Created by Federico Favotto on 09/12/2016.
 */

public interface Playlist extends List<Song>, Parcelable {
    /**
     * Name of the playlist
     * @return string that represents playlist identifier
     */
    String getName();

    /**
     * Change name of playlist
     */
    void setName(String name);

    //Insert by Munerato

    /**
     * Return the index of the song
     * @param song searched on playlist
     * @return int the index of the song, -1 if not found
     */
    int getSongIndex(Song song);

    /**
     * Set the song's index in the current playlist
     * @param song that we want to modify
     * @param index the position that we want
     * @return an boolean: false if the position is already occupied, true otherwise
     */
    boolean setSongIndex(Song song, int index);

    //Modified by Munerato
    //The methods below are useless

    /**
     * Internal unique Id of playlist
     * -1 = new playlist
     * @return the id as integer
     */
    int getId();

    /**
     * set the id of playlist
     * @param newId the new value of it
     */
    void setId(int newId);
}
