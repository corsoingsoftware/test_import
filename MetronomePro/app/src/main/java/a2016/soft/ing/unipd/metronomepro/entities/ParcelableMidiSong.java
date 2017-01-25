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

    private int id, duration;
    private String title, path;
    private final int ID_NULL = -1;

    ParcelableMidiSong(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.path = in.readString();
        this.duration = in.readInt();
    }

    ParcelableMidiSong(){
        this.id = ID_NULL;
    }

    ParcelableMidiSong(int id){
        this(id, null, null, 0);
    }

    ParcelableMidiSong(int id, String path){
        this(id, path, null, 0);
    }

    ParcelableMidiSong(int id, String path, String title){
        this(id, path, title, 0);
    }

    ParcelableMidiSong(int id, String path, String title, int duration){
        this.id = id;
        this.title = title;
        this.path = path;
        this.duration = duration;
    }


    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String newPath) { this.path = newPath; }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void setDuration(int millisDuration) { this.duration = millisDuration; }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public void setName(String newName) { this.title = newName; }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int newId) { this.id = newId; }

    @Override
    public SongPlayer getSongPlayer(SongPlayerManager manager) { return manager.getMidiSongPlayer(); }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(path);
        dest.writeInt(duration);
    }
}
