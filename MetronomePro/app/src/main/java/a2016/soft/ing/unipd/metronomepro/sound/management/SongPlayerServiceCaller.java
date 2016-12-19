package a2016.soft.ing.unipd.metronomepro.sound.management;

import a2016.soft.ing.unipd.metronomepro.MetronomeActivity;
import a2016.soft.ing.unipd.metronomepro.SelectNextSongs;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
/**
 * Created by giuli on 09/12/2016.
 */

public class SongPlayerServiceCaller implements SongPlayer {

    private static final String LOG_TAG = "SPScaller"; //for debug
    private SongPlayerService pService;
    private boolean pBound; //serve a verificare la connessione al servizio
    private ServiceConnection pConnection;
    private Context activityContext;

    public interface SongPlayerServiceCallerCallback {
        void serviceConnected();
    }

    /*costruttore gli passa un metronomeActivity (provvisorio)
    per federico: avevi detto che il fatto di passare al costruttore un oggetto di tipo metronomeActivity
                    era una cosa che facevi tu ma non posso impostare un servizio senza il contesto

     */
    public SongPlayerServiceCaller(SelectNextSongs c, final SongPlayerServiceCallerCallback callback) {
        pBound = false;
        activityContext = c;
        pConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.v(LOG_TAG,"in onServiceConnected");
                //cast
                SongPlayerService.MyBinder binder = (SongPlayerService.MyBinder) service;
                //chiamo il metodo della sottoclasse di SongPlayerService
                pService = binder.getService();
                //Chiamo il metodo onServiceInitialized di MetronomeActivity.
                callback.serviceConnected();

                if(pService != null)
                    Log.i("service-bind", "Service is bonded successfully!");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.v(LOG_TAG,"in onServiceDisconnected");
                pBound = false;

            }
        };
        //Creo il pacchetto Intent che conterrà il context e la classe del servizio.
        Intent intent_connection = new Intent(activityContext, SongPlayerService.class);

        //Connessione.
        activityContext.bindService(intent_connection, pConnection, Context.BIND_AUTO_CREATE);

        pBound = true;
    }

    @Override
    public void initialize(int frequencyBeep, int lenghtBeep, int frequencyBoop, int lenghtBoop, int sampleRate, int audioFormat, int channelConfig){
        Log.v(LOG_TAG,"in initialize(7param)");
        pService.initialize(frequencyBeep,lenghtBeep,frequencyBoop,lenghtBoop,sampleRate,audioFormat,channelConfig);
    }

    @Override
    public void initialize(int sampleRate, int audioFormat, int channelConfig){
        Log.v(LOG_TAG,"in initialize(3param)");
        pService.initialize(sampleRate,audioFormat,channelConfig);
    }

    @Override
    public void initialize(){
        Log.v(LOG_TAG,"in initialize()");
        pService.initialize();
    }

    @Override
    public void play(){
        Log.v(LOG_TAG,"in play");
        pService.play();
    }

    @Override
    public void pause(){
        Log.v(LOG_TAG,"in pause");
        pService.pause();
    }

    @Override
    public void stop(){
        Log.v(LOG_TAG,"in stop");
        pService.stop();
    }

    @Override
    public byte[] getSong(Song s){
        Log.v(LOG_TAG,"in getSong");
        return pService.getSong(s);
    }

    @Override
    public PlayState getState(){
        Log.v(LOG_TAG,"in getState");
        return pService.getState();
    }

    @Override
    public void load(Song song){
        Log.v(LOG_TAG,"in load");
        pService.load(song);

    }

    public void write(Song[] songs) {
        Log.v(LOG_TAG,"in write");
        pService.write(songs);
    }

    public void startAudioTrackSongPlayer(AudioTrackSongPlayer.AudioTrackSongPlayerCallback callback) {
        Log.v(LOG_TAG,"in startAudioTrackSongPlayer");
        pService.startAudioTrackSongPlayer(callback);
    }

    public SongPlayerService getService(){
        return pService;
    }

}
