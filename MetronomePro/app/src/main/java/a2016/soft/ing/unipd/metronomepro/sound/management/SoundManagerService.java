package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import a2016.soft.ing.unipd.metronomepro.R;

import static a2016.soft.ing.unipd.metronomepro.sound.management.SoundServiceConstants.INITIAL_VALUE;
import static a2016.soft.ing.unipd.metronomepro.sound.management.SoundServiceConstants.MAX;
import static a2016.soft.ing.unipd.metronomepro.sound.management.SoundServiceConstants.MIN;

/**
 * Riceve le chiamate in entrata che gli fa SoundManagerServiceCaller
 * e le passa ad AudiotrackController
 * Contiene al suo interno un AudioTrackController e lo gestisce correttamente in base alle chiamate che gli arrivano
 * E' un servizio di tipo Bound
 */
public class SoundManagerService extends Service {

    private static final String LOG_TAG = "SoundManagerService";        //aggiungo una stringa di LOG per facilitare il debug
    private IBinder mBinder = new myBinder();
    private AudioTrackController atc;
    private boolean isPlaying = false;                                  //di default non il metronomo non sta suonando

    @Override
    public void onCreate() {
        Log.v(LOG_TAG, "in onCreate");
        super.onCreate();
        this.atc = new AudioTrackController();
/**
 * divido  afdClack e afdClackFinal per implementare più tardi la ritmica, dove il primo sarà il clack normale
 * mentre il final sarà quello di inizio battuta
 */
        AssetFileDescriptor afdClack = null;
        try {
            afdClack = getApplicationContext().getAssets().openFd(getApplicationContext().getString(R.string.fileAudioName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        AssetFileDescriptor afdClackFinal = null;
        try {
            afdClackFinal = getApplicationContext().getAssets().openFd(getApplicationContext().getString(R.string.fileAudioName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.atc.loadFile(afdClack.getFileDescriptor(), afdClackFinal.getFileDescriptor());
        this.atc.initialize(MIN, MAX);
        this.atc.setBPM(INITIAL_VALUE);
    }
    /**
     *  i vari metodi ricevono i comandi da SoundManagerServiceCaller chiamano quelli di AudioTrackController
     *  i controlli sui parametri in ingresso sono stati tutti spostati nella classe AudioTrackController per continuità
     */

    /**
     * inizializza AudioTrackController con i valori di dafault min, max nella classe SoundManagerServiceCaller
     */
    public void initialize(int min, int max) {
        Log.v(LOG_TAG, "in initialize");
        atc.initialize(min, max);
    }


    /**
     * Ritorna il numero bpm attualmente impostato
     *
     * @return bpm
     */
    public int getBPM() {
        Log.v(LOG_TAG, "in getBPM");
        return atc.getCurrBPM();
    }

    /**
     * Stato del player
     *
     * @return 0 none/stop 1 play 2 pause
     */
    public int getState() {
        Log.v(LOG_TAG, "in getState");
        return atc.getState();
    }


    /**
     * nella classe AudioTrackController il metodo dovrà controllare che non ci siano altri suoni in esecuzione,
     * se c'è già qualcosa in esecuzione ignora la chiamata
     */
    public void play() {
        Log.v(LOG_TAG, "in play");
        atc.play();
    }

    /**
     * nella classe AudioTrackController il metodo dovrà controllare che ci sia un altro suono in esecuzione,
     * in caso contrario ignora la chiamata
     */
    public void stop() {
        Log.v(LOG_TAG, "in stop");
        atc.stop();
    }


    /**
     * Riceve il nuovo numero di bpm da SoundManagerServiceCaller e lo passa ad AudioTrackController
     * nella classe AudioTrackController ci dovrà essere un controllo sulla validità del numero passato
     *
     * @param BPM
     */
    public void setBPM(int BPM) {
        Log.v(LOG_TAG, "in setBPM");
        atc.setBPM(BPM);
    }

    @Override
    /**
     * questi sono il metodo e la classe per la creazione del service Bound
     */
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");
        return mBinder;
    }

    public class myBinder extends Binder {
        SoundManagerService getService() {
            return SoundManagerService.this;
        }
    }
}
