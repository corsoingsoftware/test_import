package a2016.soft.ing.unipd.metronomepro.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Omar on 12/12/2016.
 */

public class PlayableSong extends ParcelableSong implements Parcelable {

    public static final int STATE_TOPLAY = 0;
    public static final int STATE_READYTOPLAY = 1;
    public static final int STATE_PLAYED = 2;

    private int playlistPosition;
    private int songState; // Description of song state

    protected PlayableSong(Parcel in) {

        super(in);
        playlistPosition = in.readInt();
        songState = in.readInt();
    }

    public static final Creator<PlayableSong> CREATOR = new Creator<PlayableSong>() {
        @Override
        public PlayableSong createFromParcel(Parcel in) {
            return new PlayableSong(in);
        }

        @Override
        public PlayableSong[] newArray(int size) {
            return new PlayableSong[size];
        }
    };

    public int getPlaylistPosition() {
        return playlistPosition;
    }

    public void setPlaylistPosition(int playlistPosition) {
        this.playlistPosition = playlistPosition;
    }

    public int getSongState() {
        return songState;
    }

    public void setSongState(int songState) {
        this.songState = songState;
    }

    public PlayableSong(Song song, int playlistPosition, int songState){

        super(song.getName());
        this.name = song.getName();
        this.timeSliceList = new ArrayList<>(song);
        this.playlistPosition = playlistPosition;
        this.songState = songState;

    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        super.writeToParcel(dest, flags);
        dest.writeInt(playlistPosition);
        dest.writeInt(songState);
    }
}
