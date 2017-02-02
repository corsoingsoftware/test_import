package a2016.soft.ing.unipd.metronomepro.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Omar on 12/12/2016.
 */

public class PlayableSong implements Parcelable {

    public static final int STATE_TOPLAY = 0;
    public static final int STATE_READYTOPLAY = 1;
    public static final int STATE_PLAYED = 2;
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
    private int playlistPosition;
    private int songState; // Description of song state
    private Song innerSong;

    protected PlayableSong(Parcel in) {

        innerSong = in.readParcelable(Song.class.getClassLoader());
        playlistPosition = in.readInt();
        songState = in.readInt();
    }

    public PlayableSong(Song song, int playlistPosition, int songState) {

        innerSong = song;
        this.playlistPosition = playlistPosition;
        this.songState = songState;

    }

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

    @Override
    public int describeContents() {
        return innerSong.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(innerSong, flags);
        dest.writeInt(playlistPosition);
        dest.writeInt(songState);
    }

    public Song getInnerSong() {
        return innerSong;
    }
}
