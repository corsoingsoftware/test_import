package a2016.soft.ing.unipd.metronomepro.entities;



import android.os.Parcelable;

import java.util.List;

import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayer;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayerManager;

/**
 * Created by Federico Favotto on 07/12/2016.
 */

public interface Song extends Parcelable {

    /**
     * returns the name of song
     * @return songname
     */
    String getName();

    /**
     * Ridenominates song
     * @param newName the new name of song
     */
    void setName(String newName);

    /**
     * returns the id of songs
     * @return the id of the song
     */
    int getId();

    /**
     * sets the new id
     * @param newId the new id
     */
    void setId(int newId);

    /**
     * return the manager for the song
     * @param manager manager that contains the songplayers
     * @return return the songplayer for this song
     */
    SongPlayer getSongPlayer(SongPlayerManager manager);
}
