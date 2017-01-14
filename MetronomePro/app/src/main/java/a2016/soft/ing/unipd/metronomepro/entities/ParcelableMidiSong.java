package a2016.soft.ing.unipd.metronomepro.entities;

import android.os.Parcel;

import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayer;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayerManager;

/**
 * Created by feder on 14/01/2017.
 */

public class ParcelableMidiSong implements MidiSong {
    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void setPath(String newPath) {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public void setDuration(int millisDuration) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String newName) {

    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int newId) {

    }

    @Override
    public SongPlayer getSongPlayer(SongPlayerManager manager) {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
