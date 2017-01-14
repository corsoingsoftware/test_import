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
    public void play(Song entrySong) {
        entrySong.getSongPlayer(this).play(entrySong);
    }

    public void load(Song entrySong) {
        entrySong.getSongPlayer(this).load(entrySong);
    }

    public void write(Song entrySong) {

        entrySong.getSongPlayer(this).write(entrySong);
    }

    public byte[] getSong(Song entrySong) {
        return entrySong.getSongPlayer(this).getSong(entrySong);
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
