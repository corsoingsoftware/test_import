package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.content.Context;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Federico Favotto on 06/01/2017.
 * Developed by Omar. Thanks Federico for helping.
 *
 * This class deals with reproducing the songs selected by the user in the order chosen by the user itself.
 * In particular it loads the songs in the appropriate songPlayers and plays them exploiting the available resources:
 * audioTrackSongPlayer and midiSongPlayer.
 */


public class MultipleSongPlayerManager implements SongPlayerManager, SongPlayer.SongPlayerCallback {

    //Players for midiSong and timeSlicesSong respectively. They are unique in the class.
    private AudioTrackSongPlayer audioTrackSongPlayer;
    private MidiSongPlayer midiSongPlayer;

    //Queue that contains songs to be loaded.
    private LinkedBlockingQueue<Song> songsToLoadQueue;

    //Contains the index of the next Song to be played.
    private int indexNextToPlay;

    //Contains the index of the next Song to be loaded.
    private int indexNextToLoad;

    //Array that contains songs to be played.
    private Song[] arraySongsToPlay;

    public MultipleSongPlayerManager(Context c) {

        audioTrackSongPlayer = new AudioTrackSongPlayer(this);
        midiSongPlayer = new MidiSongPlayer(c, this);
    }

    /**
     * Method that initializes the attributes with initial values and SongQueue with arrayEntrySongs' elements.
     * @param arrayEntrySongs  Array that contains songs to be played.
     */

    public void startTheseSongs(Song[] arrayEntrySongs) {

        arraySongsToPlay = arrayEntrySongs;
        indexNextToPlay = 0;
        indexNextToLoad = 0;
        songsToLoadQueue = new LinkedBlockingQueue<>();

        for(Song songToAdd : arrayEntrySongs){
            songsToLoadQueue.add(songToAdd);
        }

        dequeueManagement();
        arraySongsToPlay[indexNextToPlay].getSongPlayer(this).play();
        checkQueueEmpty();
    }

    /**
     *  It manages the loading of the songs. In particular it identifies blocks of songs
     *  of the same type in the queue, remove them from it and loads them in the appropriate SongPlayer.
     */

    public void dequeueManagement(){

        LinkedList<Song> listSongsSameType = new LinkedList<Song>();
        Song currentSong = songsToLoadQueue.peek();
        Class currentSongClass = currentSong.getClass();
        SongPlayer currentSongSongPlayer = currentSong.getSongPlayer(this);

        while(songsToLoadQueue.size() > 0 && currentSong.getClass() == currentSongClass) {
            songsToLoadQueue.poll();
            listSongsSameType.add(currentSong);
            currentSong = songsToLoadQueue.peek();
        }

        Song[] arraySongsSameType = new Song[listSongsSameType.size()];
        indexNextToLoad = indexNextToLoad + listSongsSameType.size();
        arraySongsSameType = listSongsSameType.toArray(arraySongsSameType);
        currentSongSongPlayer.write(arraySongsSameType);
    }

    /**
     * It returns a Player for midiSongs. It will be transparently called if the song examined is an instance of
     * MidiSong thanks to Song interface and its implementation in ParcelableMidiSong.
     * @return midiSongPlayer SongPlayer for midiSongs.
     */

    @Override
    public SongPlayer getMidiSongPlayer() {

        return midiSongPlayer;
    }

    /**
     * Similar to getMidiSongPlayer().
     * @return audioTrackSongPlayer SongPlayer for timeSlicesSongs.
     */

    @Override
    public SongPlayer getTimeSlicesSongPlayer() {

        return audioTrackSongPlayer;
    }

    /**
     * Method called when a SongPlayer (audioTrackSongPlayer or midiSongPlayer) stopped reproducing its loaded songs.
     * It reproduces next ready Song or, if all songs have been played, calls pause method in both players.
     * @param origin SongPlayer stopped reproducing its songs.
     */

    @Override
    public void playEnded(SongPlayer origin) {

        if(indexNextToPlay < arraySongsToPlay.length) {
            origin.pause();
            arraySongsToPlay[indexNextToPlay].getSongPlayer(this).play();
            checkQueueEmpty();

        } else {

            audioTrackSongPlayer.pause();
            midiSongPlayer.pause();
        }
    }

    /**
     *  Method that updates indexNextToPlay and checks if the queue is empty.
     *  If not, it calls dequeueManagement() to continue songs' loading.
     */

    private void checkQueueEmpty() {

        indexNextToPlay = indexNextToLoad;
        if(indexNextToLoad < arraySongsToPlay.length)
            dequeueManagement();
    }
}
