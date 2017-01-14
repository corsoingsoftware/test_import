package a2016.soft.ing.unipd.metronomepro.entities;



import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayer;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayerManager;

/**
 * Created by Federico Favotto on 07/12/2016.
 */

public abstract class Song implements Parcelable {

    public final Creator<Song> CREATOR = new Creator<Song>() {
        /**
         * create song from parcel, just call default parcel constructor
         * @param in
         * @return
         */
        @Override
        public Song createFromParcel(Parcel in) {

            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };




    /**
     * returns the name of song
     * @return songname
     */
    abstract String getName();

    /**
     * Ridenominates song
     * @param newName the new name of song
     */
    abstract void setName(String newName);

    /**
     * returns the id of songs
     * @return the id of the song
     */
    abstract int getId();

    /**
     * sets the new id
     * @param newId the new id
     */
    abstract void setId(int newId);

    /**
     * return the manager for the song
     * @param manager manager that contains the songplayers
     * @return return the songplayer for this song
     */
    abstract SongPlayer getSongPlayer(SongPlayerManager manager);
}
