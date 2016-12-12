package a2016.soft.ing.unipd.metronomepro.entities;

import java.util.ArrayList;

/**
 * Created by Omar on 12/12/2016.
 */

public class PlayableSong extends ParcelableSong{

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

    private int playlistPosition;
    private int songState; // Description

    public PlayableSong(Song song, int playlistPosition, int songState){
        this.name = song.getName();
        this.timeSliceList = new ArrayList<>(song);
        this.playlistPosition = playlistPosition;
        this.songState = songState;
    }
}
