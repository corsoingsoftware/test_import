package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by giuli on 09/12/2016.
 */

public class SongPlayerService extends Service {

    private static final String LOG_TAG = "SongPlayerService";
    private IBinder mBinder = new MyBinder();
    private SongPlayer atsp;

    @Override
    public void onCreate(){
        Log.v(LOG_TAG,"in onCreate");
        super.onCreate();
        this.atsp = new AudioTrackSongPlayer();
    }
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void initialize(int frequencyBeep, int lenghtBeep, int frequencyBoop, int lenghtBoop, int sampleRate, int audioFormat, int channelConfig){
        atsp.initialize(frequencyBeep,lenghtBeep,frequencyBoop,lenghtBoop,sampleRate,audioFormat,channelConfig);
        Log.v(LOG_TAG,"in initialize(7param)");
    }

    public void initialize(int sampleRate, int audioFormat, int channelConfig){
        atsp.initialize(sampleRate,audioFormat,channelConfig);
        Log.v(LOG_TAG,"in initialize(3param)");
    }

    public void initialize(){
        atsp.initialize();
        Log.v(LOG_TAG,"in initialize()");
    }

    public void play(){
        atsp.play();
        Log.v(LOG_TAG,"in play");
    }

    public void pause(){
        atsp.pause();
        Log.v(LOG_TAG,"in pause");
    }

    public void stop(){
        atsp.stop();
        Log.v(LOG_TAG,"in stop");
    }

    public byte[] getSong(Song s){
        Log.v(LOG_TAG,"in getSong");
        return atsp.getSong(s);
    }

    public PlayState getState(){
        Log.v(LOG_TAG,"in getState");
        return atsp.getState();
    }

    public void load(Song song){
        atsp.load(song);
        Log.v(LOG_TAG,"in load");

    }
    public class MyBinder extends Binder {
        SongPlayerService getService() {
            return SongPlayerService.this;
        }
    }
}