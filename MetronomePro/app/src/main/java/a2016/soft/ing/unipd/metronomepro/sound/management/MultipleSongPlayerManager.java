package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.content.Context;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;

/**
 * Created by Federico Favotto on 06/01/2017.
 * Developed by Omar, thanks Federico for the help.
 */

public class MultipleSongPlayerManager implements SongPlayerManager, SongPlayer.SongPlayerCallback {

    private AudioTrackSongPlayer audioTrackSongPlayer;
    private MidiSongPlayer midiSongPlayer;
    private LinkedBlockingQueue<Song> songQueue;
    private int nextToPlay;
    private int nextToLoad;
    private Song[] arraySongsToPlay;

    public MultipleSongPlayerManager(Context c) {

        audioTrackSongPlayer = new AudioTrackSongPlayer(this);
        midiSongPlayer = new MidiSongPlayer(c, this);
    }

    public void load(Song entrySong) {
        entrySong.getSongPlayer(this).load(entrySong);
    }

    public void startTheseSongs(Song[] songs) {

        arraySongsToPlay = songs;
        nextToPlay = 0;
        nextToLoad = 0;
        songQueue= new LinkedBlockingQueue<>();

        for(Song song : songs){
            songQueue.add(song);
        }

        dequeueManagement();
        arraySongsToPlay[nextToPlay].getSongPlayer(this).play();
        checkQueueEnded();
    }

    public void dequeueManagement(){

        LinkedList<Song> listSongsSameType = new LinkedList<Song>();
        Song currSong = songQueue.peek();
        Class s = currSong.getClass();
        SongPlayer currentPlayer = currSong.getSongPlayer(this);

        while(songQueue.size() > 0 && currSong.getClass() == s) {
            songQueue.poll();
            listSongsSameType.add(currSong);
            currSong = songQueue.peek();
        }

        Song[] app = new Song[listSongsSameType.size()];
        nextToLoad += listSongsSameType.size();
        app = listSongsSameType.toArray(app);
        currentPlayer.write(app);
    }

    @Override
    public SongPlayer getMidiSongPlayer() {
        return midiSongPlayer;
    }

    @Override
    public SongPlayer getTimeSlicesSongPlayer() {
        return audioTrackSongPlayer;
    }

    @Override
    public void playEnded(SongPlayer origin) {
        if(nextToPlay < arraySongsToPlay.length) {
            origin.pause();
            arraySongsToPlay[nextToPlay].getSongPlayer(this).play();
            checkQueueEnded();

        } else {

            //avviso l'activity che ho finito
        }
    }

    private void checkQueueEnded() {
        nextToPlay = nextToLoad;
        if(nextToLoad < arraySongsToPlay.length)
            dequeueManagement();
    }
}
