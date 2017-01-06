package a2016.soft.ing.unipd.metronomepro.sound.management;

import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Federico Favotto on 06/01/2017.
 */

public class MultipleSongPlayerManager implements SongPlayerManager {
    private AudioTrackSongPlayer audioTrackSongPlayer;

    /**
     * Example of method to manage a generic song
     */
    public void play(Song song){
        song.getSongPlayer(this).play();
    }

    @Override
    public SongPlayer getMidiSongPlayer() {
        return null;
    }

    @Override
    public SongPlayer getTimeSlicesSongPlayer() {
        return audioTrackSongPlayer;
    }
    //altra istanza di un eventuale midiplayer
}
