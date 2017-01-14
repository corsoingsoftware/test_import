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
