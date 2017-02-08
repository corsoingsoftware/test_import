package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import a2016.soft.ing.unipd.metronomepro.entities.MidiSong;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableMidiSong;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Munerato, Alberto on 14/01/17.
 */

public class MidiSongPlayer implements SongPlayer, MediaPlayer.OnCompletionListener {

    public final String TAG = "MidiSongPlayer";
    private MediaPlayer actualPlayer;
    private SongPlayerCallback callback;
    private List<String> songList;
    private Iterator<String> currentSong;
    private Context context;
    private HashMap <String, MediaPlayer> playerList;
    private final Iterator NULL_POINTER = null;
    private final MediaPlayer NOT_INITIALIZED = null;

    MidiSongPlayer(Context c, SongPlayerCallback callback){
        currentSong = NULL_POINTER;
        actualPlayer = NOT_INITIALIZED;
        this.callback = callback;
        this.context = c;
        playerList = new HashMap<>();
    }

    @Override
    public void play() {

        actualPlayer.start();
    }

    @Override
    public void pause() {
        if(actualPlayer!=null&&actualPlayer.isPlaying())
            actualPlayer.pause();
    }

    @Override
    public void stop() {
        actualPlayer.stop();
    }

    @Override
    public void load(Song song) {
        String audioPath = ((MidiSong) song).getPath();
        MediaPlayer playerToAdd = getMediaPlayer(audioPath);
        if(playerToAdd != null) playerList.put(song.getName(), playerToAdd);
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
        return PlayState.PLAYSTATE_UNKNOW;
    }

    @Override
    public void write(Song[] songs) {
        songList=new ArrayList<>(200);
        for (Song song : songs) songList.add(song.getName());
        currentSong = songList.iterator();
        actualPlayer = playerList.get(currentSong.next());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(currentSong.hasNext()){
            actualPlayer = playerList.get(currentSong.next());
            actualPlayer.start();
        } else {
            callback.playEnded(this);
            songList = null;
            currentSong = null;
            actualPlayer = null;
        }
    }

    /**
     * Federico: il player va creato ogni volta che si cambia la traccia!
     */
    private MediaPlayer getMediaPlayer(String audioPath) {
        MediaPlayer newMediaPlayer = new MediaPlayer();
        newMediaPlayer.setOnCompletionListener(this);
        newMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Uri firstSongPath = Uri.parse(audioPath);
        try {
            newMediaPlayer.setDataSource(context, firstSongPath);
            newMediaPlayer.prepare();
        }catch(Exception e){
            e.printStackTrace();
            Log.d(TAG,"Midi not found or invalid path");
            return null;
        }
        return newMediaPlayer;
    }
}
