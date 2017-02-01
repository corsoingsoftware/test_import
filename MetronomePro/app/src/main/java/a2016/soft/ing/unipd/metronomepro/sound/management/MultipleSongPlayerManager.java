package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.content.Context;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Federico Favotto on 06/01/2017.
 * Developed by Omar, thanks Federico for the help.
 */

public class MultipleSongPlayerManager implements SongPlayerManager, SongPlayer.SongPlayerCallback {

    private AudioTrackSongPlayer audioTrackSongPlayer;
    private MidiSongPlayer midiSongPlayer;
    private LinkedBlockingQueue<Song> songsQueue;
    //Contains the index of the next song to be played.
    private int indexNextToPlay;
    //Contains the index of the next song to be loaded.
    private int indexNextToLoad;
    //Array that contains songs to be played.
    private Song[] arraySongsToPlay;

    public MultipleSongPlayerManager(Context c) {

        audioTrackSongPlayer = new AudioTrackSongPlayer(this);
        midiSongPlayer = new MidiSongPlayer(c, this);
    }

    public void load(Song entrySong) {
        entrySong.getSongPlayer(this).load(entrySong);
    }

    /**
     * Method that initializes the attributes with default values and SongQueue with arrayEntrySongs' elements.
     * @param arrayEntrySongs  Array that contains songs to be played.
     */

    public void startTheseSongs(Song[] arrayEntrySongs) {

        arraySongsToPlay = arrayEntrySongs;
        indexNextToPlay = 0;
        indexNextToLoad = 0;


        songsQueue = new LinkedBlockingQueue<>();

        for(Song songToAdd : arrayEntrySongs){
            songsQueue.add(songToAdd);
        }

        dequeueManagement();
        arraySongsToPlay[indexNextToPlay].getSongPlayer(this).play();
        checkQueueEmpty();
    }

    /**
     *  It manages the loading of the songs. In particular it identifies blocks of the songs
     *  of the same type in the queue, remove them from it and loads them in the appropriate Player.
     */

    public void dequeueManagement(){

        LinkedList<Song> listSongsSameType = new LinkedList<Song>();
        Song currentSong = songsQueue.peek();
        Class currentSongClass = currentSong.getClass();
        SongPlayer currentSongSongPlayer = currentSong.getSongPlayer(this);

        while(songsQueue.size() > 0 && currentSong.getClass() == currentSongClass) {
            songsQueue.poll();
            listSongsSameType.add(currentSong);
            currentSong = songsQueue.peek();
        }

        Song[] arraySongsSameType = new Song[listSongsSameType.size()];
        indexNextToLoad = indexNextToLoad + listSongsSameType.size();
        arraySongsSameType = listSongsSameType.toArray(arraySongsSameType);
        currentSongSongPlayer.write(arraySongsSameType);
    }

    /**
     * It returns a player for midiSongs. It will be automatically called if the song examined is a MidiSong
     * thanks to Song interface and its implementation in MidiSong.
     * @return midiSongPlayer A player for MidiSongs.
     */

    @Override
    public SongPlayer getMidiSongPlayer() {
        return midiSongPlayer;
    }

    /**
     * Similar to getMidiSongPlayer().
     * @return audioTrackSongPlayer A player for TimeSlicesSongs.
     */

    @Override
    public SongPlayer getTimeSlicesSongPlayer() {
        return audioTrackSongPlayer;
    }

    /**
     * Method called when a Player (AudioTrackSongPlayer or MidiPlayer) finished to reproduce its loaded songs.
     * @param origin SongPlayer that finished to play the songs.
     */

    @Override
    public void playEnded(SongPlayer origin) {

        if(indexNextToPlay < arraySongsToPlay.length) {
            origin.pause();
            arraySongsToPlay[indexNextToPlay].getSongPlayer(this).play();
            checkQueueEmpty();

        } else {

            try {
                audioTrackSongPlayer.pause();
                midiSongPlayer.pause();
            } catch (Exception e) {

            }

            //avviso l'activity che ho finito
        }
    }

    /**
     *  Method that checks if the queue is empty. If not, it calls dequeueManagement() to continue
     *  the loading of the songs.
     */

    private void checkQueueEmpty() {

        indexNextToPlay = indexNextToLoad;
        if(indexNextToLoad < arraySongsToPlay.length)
            dequeueManagement();
    }
}
