package a2016.soft.ing.unipd.metronomepro.sound.management;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;

/**
 * Created by Federico Favotto on 06/01/2017.
 */

public class MultipleSongPlayerManager implements SongPlayerManager {

    private final static int PLAYERS=2;
    private AudioTrackSongPlayer audioTrackSongPlayer;
    private int timeSlicesPlayerState;
    private int midiPlayerState;
    private LinkedBlockingQueue<Song> songQueue;


    /** player state
     * 0 := puoi fare write
     * 1 := puoi fare play
     * 2 := in play
     */

    public MultipleSongPlayerManager() {

        //audioTrackSongPlayer = new AudioTrackSongPlayer();
        timeSlicesPlayerState = 0;
        midiPlayerState = 0;
        songQueue = new LinkedBlockingQueue<Song>();
    }

    /**
     * Example of method to manage a generic song
     */
    public void play(Song entrySong) {
        entrySong.getSongPlayer(this).play();
    }

    public void load(Song entrySong) {
        entrySong.getSongPlayer(this).load(entrySong);
    }

    public void write(Song[] songs) {

        //entrySong.getSongPlayer(this).write(entrySong);

        int i = 0;
        int typeChanged=0;
        Class s=songs[i].getClass();
        while(i < songs.length && typeChanged<PLAYERS) {
            Song currSong = songs[i++];
            if(currSong.getClass()!=s){
                typeChanged++;
            }
            while(songs[i] instanceof currSong. && i < songs.length) {

                songQueue.add(songs[i]);
                i++;
            }
        }

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
