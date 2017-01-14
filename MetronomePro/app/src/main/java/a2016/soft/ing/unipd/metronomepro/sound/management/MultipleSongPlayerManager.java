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
    private int timeSlicesPlayerState;
    private int midiPlayerState;
    private LinkedBlockingQueue<Song> songQueue;
    private int typeChanged;


    /** player state
     * 0 := puoi fare write
     * 1 := puoi fare play
     * 2 := in play
     */

    public MultipleSongPlayerManager() {

        audioTrackSongPlayer = new AudioTrackSongPlayer(this);
        timeSlicesPlayerState = 0;
        midiPlayerState = 0;
        songQueue = new LinkedBlockingQueue<Song>();
        typeChanged = 0;
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

    /*public void write(Song[] songs, Song[] ) {

        //entrySong.getSongPlayer(this).write(entrySong);

        int i = 0;
        int typeChanged = 0;
        Class s = songs[i].getClass();
        Song currSong = songs[i++];

        while(i < songs.length && typeChanged < PLAYERS) {

            if(currSong.getClass() != s){
                typeChanged++;
                currSong = songs[i];
                s = songs[i].getClass();
            }
            else {
                songQueue.add(songs[i]);
            }

            i++;
        }


    }*/


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
    }

    //altra istanza di un eventuale midiplayer
}
