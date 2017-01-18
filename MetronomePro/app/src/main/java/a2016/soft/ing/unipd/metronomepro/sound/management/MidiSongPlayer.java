package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import a2016.soft.ing.unipd.metronomepro.entities.MidiSong;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Munerato, Alberto on 14/01/17.
 */

public class MidiSongPlayer implements SongPlayer, MediaPlayer.OnCompletionListener {

    public final String TAG = "MidiSongPlayer";
    private final int UNPOINTED = -1;
    private MediaPlayer player;
    private int playerState;
    private final int ON_PLAY = 0, ON_PAUSE = 1, ON_STOP = 2;
    private SongPlayerCallback callback;
    private MidiSong[] playlist;
    private int currentSong;

    MidiSongPlayer(Context c){
        playerState = ON_STOP;
        currentSong = UNPOINTED;
        player= new MediaPlayer();
        player.setOnCompletionListener(this);
    }

    @Override
    public void play() {
        if(playerState != ON_PLAY) {
            playerState = ON_PLAY;
            player.start();
        }
    }

    @Override
    public void pause() {
        if(playerState == ON_PLAY) {
            playerState = ON_PAUSE;
            player.pause();
        }
    }

    @Override
    public void stop() {
        if(playerState != ON_STOP) {
            playerState = ON_STOP;
            player.stop();
        }
    }

    @Override
    public void load(Song song) {

    }

    /**
     * This method is useless at this implementation
     * @param s song to search
     * @return
     */
    @Override
    public byte[] getSong(Song s) {
        return new byte[0];
    }

    @Override
    public PlayState getState() {
        PlayState ps;
        switch(playerState) {
            case ON_PLAY :  ps = PlayState.PLAYSTATE_PLAYING;
            case ON_PAUSE : ps = PlayState.PLAYSTATE_PAUSE;
            case ON_STOP : ps = PlayState.PLAYSTATE_STOP;
            default: ps = null;
        }
        return ps;
    }


    @Override
    public void write(Song[] songs) {
        currentSong = 0;
        playlist = new MidiSong[songs.length];
        for(int i = 0; i < songs.length; i++) playlist[i] = (MidiSong) songs[i];
        try {
            player.setDataSource(playlist[currentSong].getPath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"Midi not found or invalid path");
        }
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        currentSong++;
        if(currentSong < playlist.length){
            try {
                player.setDataSource(playlist[currentSong].getPath());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG,"Midi not found or invalid path");
            }
            try {
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.start();
        }
        else{
            currentSong = UNPOINTED;
            playlist = null;
            callback.playEnded(this);
        }
    }
}
