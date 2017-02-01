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
 * Developed by Omar
 */

public class MultipleSongPlayerManager implements SongPlayerManager, SongPlayer.SongPlayerCallback {

    private final static int PLAYERS = 2;
    private AudioTrackSongPlayer audioTrackSongPlayer;
    private MidiSongPlayer midiSongPlayer;
    private LinkedBlockingQueue<Song> songQueue;
    private int typeChanged;
    private int nextToPlay;
    private Song[] arraySongs;


    public MultipleSongPlayerManager(Context c) {

        audioTrackSongPlayer = new AudioTrackSongPlayer(this);
        midiSongPlayer = new MidiSongPlayer(c, this);
        songQueue = new LinkedBlockingQueue<Song>();
        typeChanged = 0;
    }

    /**
     * Example of method to manage a generic song
     */

    public void play(Song[] songs) {

        Class currClass = songs[nextToPlay].getClass();
        int i = nextToPlay;
        Song currSong = songs[i];
        currSong.getSongPlayer(this).play();

        //Individuo la prossima song da riprodurre in seguito al playEnded

        boolean stopLoop = false;
        while(i < songs.length && !(stopLoop)) {

            if(songs[i].getClass() == currClass)
                i++;
            else
                stopLoop = true;
        }

        nextToPlay = i;

        if(songQueue.size() > 0){
            dequeueManagement(songs);
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

            arraySongs = songs;
            nextToPlay = 0;
            dequeueManagement(arraySongs);
        }

    }

    public void dequeueManagement(Song[] songs){


        LinkedList<Song> listSongsSameType = new LinkedList<Song>();
        Song currSong = songQueue.peek();
        Class s = currSong.getClass();
        SongPlayer currentPlayer = currSong.getSongPlayer(this);

        while(typeChanged < PLAYERS && songQueue.size() !=0) {

            if(currSong.getClass() != s) {

                typeChanged ++;

                if(typeChanged <= PLAYERS) {

                    Song[] app = new Song[listSongsSameType.size()];
                    app = listSongsSameType.toArray(app);
                    currentPlayer.write(app);
                    listSongsSameType.clear();
                    currentPlayer = currSong.getSongPlayer(this);
                    s = currSong.getClass();
                    listSongsSameType.add(currSong);
                    songQueue.poll();
                }

            }
            else {

                listSongsSameType.add(currSong);
                songQueue.poll();

            }

            currSong = songQueue.peek();

            if(songQueue.size()==0) {
                Song[] app = new Song[listSongsSameType.size()];
                app = listSongsSameType.toArray(app);
                currentPlayer.write(app);
            }
        }

        play(arraySongs);
    }


    public byte[] getSong(Song entrySong) {
        return entrySong.getSongPlayer(this).getSong(entrySong);
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

        if(nextToPlay < arraySongs.length) {
            typeChanged--;
            play(arraySongs);
        }
    }

}
