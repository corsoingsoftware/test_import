package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import a2016.soft.ing.unipd.metronomepro.entities.MidiSong;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Alberto on 14/01/17.
 */

public class MidiSongPlayer implements SongPlayer, MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    private int playerState;
    private final int ON_PLAY = 0, ON_PAUSE = 1, ON_STOP = 3;
    private SongPlayerCallback callback;
    private MidiSong[] playlist;
    private int currentSong;

    MidiSongPlayer(Context c){
        playerState = ON_STOP;

        player.create(c, null);
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
        if(playerState == ON_PLAY) {
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
        if(playerState == ON_PLAY){
            ps = PlayState.PLAYSTATE_PLAYING;
        }else if(playerState == ON_PAUSE){

        }
        return null;
    }


    @Override
    public void write(Song[] songs) {
        currentSong = 0;
        //for(song:songs) playlist[song] = (MidiSong) songs[i++];
        //player.setDataSource(playlist[currentSong].getPath());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        callback.playEnded();
    }
}
