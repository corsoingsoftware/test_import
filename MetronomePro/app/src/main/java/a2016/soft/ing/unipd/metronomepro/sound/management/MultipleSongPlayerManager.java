package a2016.soft.ing.unipd.metronomepro.sound.management;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;

/**
 * Created by Federico Favotto on 06/01/2017.
 */

public class MultipleSongPlayerManager implements SongPlayerManager, SongPlayer.SongPlayerCallback {

    private final static int PLAYERS=2;
    private AudioTrackSongPlayer audioTrackSongPlayer;
    private MidiSongPlayer midiSongPlayer;
    private LinkedBlockingQueue<Song> songQueue;
    private int typeChanged;


    public MultipleSongPlayerManager() {

        audioTrackSongPlayer = new AudioTrackSongPlayer(this);
        songQueue = new LinkedBlockingQueue<Song>();
        typeChanged = 0;
    }

    /**
     * Example of method to manage a generic song
     */
    public void play(Song[] songs) {

        //entrySong.getSongPlayer(this).play();

        Class currClass = songs[0].getClass();

        for(int i = 0; i < songs.length; i++) {

            if(songs[i].getClass() != currClass) {

            }
            else {

            }
        }
    }

    public void load(Song entrySong) {
        entrySong.getSongPlayer(this).load(entrySong);
    }


    public void startTheseSongs(Song[] songs) {

        if(songs != null) {

            for (Song s : songs) {
                songQueue.add(s);
            }
            dequeueManagement();
        }
    }

    public void dequeueManagement(){

        LinkedList<Song> listSongsSameType = new LinkedList<Song>();
        Class s = (songQueue.peek()).getClass();
        Song currSong = songQueue.poll();
        SongPlayer currentPlayer = currSong.getSongPlayer(this);

        while(typeChanged < PLAYERS && songQueue.size() !=0) {

            if(currSong.getClass() != s) {

                currentPlayer.write((Song[])listSongsSameType.toArray());
                typeChanged ++;

                if(typeChanged < PLAYERS) {
                    s = currSong.getClass();
                    currentPlayer = currSong.getSongPlayer(this);
                    listSongsSameType.clear();
                    listSongsSameType.add(currSong);
                    currSong = songQueue.poll();
                }
            }
            else {

                listSongsSameType.add(currSong);
                currSong = songQueue.poll();
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

    @Override
    public void playEnded(SongPlayer origin) {

        typeChanged --;
        dequeueManagement();
        //play();
    }

}
