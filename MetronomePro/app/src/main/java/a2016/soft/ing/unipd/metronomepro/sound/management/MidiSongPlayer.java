package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import a2016.soft.ing.unipd.metronomepro.entities.MidiSong;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableMidiSong;
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
    private Context context;

    MidiSongPlayer(Context c, SongPlayerCallback callback){
        playerState = ON_STOP;
        currentSong = UNPOINTED;
        this.callback = callback;
        this.context = c;
    }

    @Override
    public void play() {
        player.start();
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
        System.out.print("LOAD");
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
        if(player.isPlaying()) {
            ps = PlayState.PLAYSTATE_PLAYING;
        } else {
            ps = PlayState.PLAYSTATE_STOP;
        }

        return ps;
    }

    /**
     * Federico: il player va creato ogni volta che si cambia la traccia!
     * This method reinitializes the mediaPlayer
     */

    private void createNewMediaPlayer(String audioPath) {
        if(player!=null){
            player.setOnCompletionListener(null);
            player.release();
            player=null;
        }
        player= new MediaPlayer();
        player.setOnCompletionListener(this);
        Uri firstSongPath = Uri.parse(audioPath);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(context, firstSongPath);
            player.prepare();
        }catch(Exception e){
            e.printStackTrace();
            Log.d(TAG,"Midi not found or invalid path");
        }
    }

    @Override
    public void write(Song[] songs) {

        currentSong = 0;
        playlist = new ParcelableMidiSong[songs.length];
        for(int i = 0; i < songs.length; i++)
            playlist[i] = (ParcelableMidiSong) songs[i];
        createNewMediaPlayer(playlist[currentSong].getPath());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        currentSong++;
        if(currentSong < playlist.length){
            try {
                createNewMediaPlayer(playlist[currentSong].getPath());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG,"Midi not found or invalid path");
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
