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
    //Array that contains the songs to be played.
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
     *  It manages the loading of the songs in theirs corresponding Players. In particular it identifies blocks of
     *  songs of the same type and loads them.
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

        Song[] app = new Song[listSongsSameType.size()];
        indexNextToLoad += listSongsSameType.size();
        app = listSongsSameType.toArray(app);
        currentSongSongPlayer.write(app);
    }

    /**
     * It returns a player for midiSongs. It is totally transparent thanks to Song interface.
     * @return midiSongPlayer A player for midiSongs.
     */

    @Override
    public SongPlayer getMidiSongPlayer() {

        return midiSongPlayer;
    }

    /**
     *
     * @return audioTrackSongPlayer
     */

    @Override
    public SongPlayer getTimeSlicesSongPlayer() {

        return audioTrackSongPlayer;
    }

    /**
     * Method called when a Player finished to play a block of songs of the same type. It
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
     *  Method for checking the
     */

    private void checkQueueEmpty() {

        indexNextToPlay = indexNextToLoad;
        if(indexNextToLoad < arraySongsToPlay.length)
            dequeueManagement();
    }
}
