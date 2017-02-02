package a2016.soft.ing.unipd.metronomepro.entities;

import android.os.Parcel;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayer;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayerManager;

/**
 * Created by Munerato, Alberto on 14/01/2017.
 */

public class ParcelableMidiSong implements MidiSong {

    public static final Creator<ParcelableMidiSong> CREATOR = new Creator<ParcelableMidiSong>() {
        @Override
        public ParcelableMidiSong createFromParcel(Parcel in) {
            return new ParcelableMidiSong(in);
        }

        @Override
        public ParcelableMidiSong[] newArray(int size) {
            return new ParcelableMidiSong[size];
        }
    };

    private String title, path;

    ParcelableMidiSong(Parcel in) {
        this.title = in.readString();
        this.path = in.readString();
    }

    ParcelableMidiSong(){
        this(null, null);
    }

    ParcelableMidiSong(String title){
        this(title, null);
    }

    ParcelableMidiSong(String title, String path){
        this.title = title;
        this.path = path;
    }


    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String newPath) { this.path = newPath; }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public void setName(String newName) { this.title = newName; }

    @Override
    public SongPlayer getSongPlayer(SongPlayerManager manager) { return manager.getMidiSongPlayer(); }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(path);
    }

    //The methods below now are useless
    @Override
    public int getId() {
        return -1;
    }

    @Override
    public void setId(int newId) {

    }

}
