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
    private LinkedBlockingQueue<Song> songQueue;
    private int indexNextToPlay;
    private int indexNextToLoad;
    private Song[] arraySongsToPlay;

    public MultipleSongPlayerManager(Context c) {

        audioTrackSongPlayer = new AudioTrackSongPlayer(this);
        midiSongPlayer = new MidiSongPlayer(c, this);
    }

    public void load(Song entrySong) {
        entrySong.getSongPlayer(this).load(entrySong);
    }

    public void startTheseSongs(Song[] arrayEntrySongs) {

        arraySongsToPlay = arrayEntrySongs;
        indexNextToPlay = 0;
        indexNextToLoad = 0;
        songQueue= new LinkedBlockingQueue<>();

        for(Song songToAdd : arrayEntrySongs){
            songQueue.add(songToAdd);
        }

        dequeueManagement();
        arraySongsToPlay[indexNextToPlay].getSongPlayer(this).play();
        checkQueueEnded();
    }

    public void dequeueManagement(){

        LinkedList<Song> listSongsSameType = new LinkedList<Song>();
        Song currentSong = songQueue.peek();
        Class currentSongClass = currentSong.getClass();
        SongPlayer currentSongSongPlayer = currentSong.getSongPlayer(this);

        while(songQueue.size() > 0 && currentSong.getClass() == currentSongClass) {
            songQueue.poll();
            listSongsSameType.add(currentSong);
            currentSong = songQueue.peek();
        }

        Song[] app = new Song[listSongsSameType.size()];
        indexNextToLoad += listSongsSameType.size();
        app = listSongsSameType.toArray(app);
        currentSongSongPlayer.write(app);
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

        if(indexNextToPlay < arraySongsToPlay.length) {
            origin.pause();
            arraySongsToPlay[indexNextToPlay].getSongPlayer(this).play();
            checkQueueEnded();

        } else {

            try {
                audioTrackSongPlayer.pause();
                midiSongPlayer.pause();
            } catch (Exception e) {

            }

            //avviso l'activity che ho finito
        }
    }

    private void checkQueueEnded() {

        indexNextToPlay = indexNextToLoad;
        if(indexNextToLoad < arraySongsToPlay.length)
            dequeueManagement();
    }
}
