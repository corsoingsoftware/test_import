package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Riceve le chiamate in entrata e gestisce Audiotrack!
 * Contiene al suo interno un audiotrackcontroller e lo gestisce correttamente in base alle chiamate che gli arrivano!!
 */
public class SoundManagerService extends Service {

    private static final String LOG_TAG = "SoundManagerService";       //aggiungo una stringa di log per facilitare il debug
    private IBinder mBinder = new myBinder();
    private AudioTrackController atc;
    private boolean isPlaying = false;
    private static final int MIN_BPM = 30;
    private static final int MAX_BPM = 300;

    public SoundManagerService() {
        this.atc = new AudioTrackController();
    }

    // i vari metodi ricevono i comandi da SoundManagerServiceCaller e dopo aver fatto i controlli chiamano i metodi
    //   di AudioTrackController


    /**
     *  controllo che il massimo e il minimo siano entro il range rispettato
     * @param lowBPM
     * @param highBPM
     */
    public void initialize(int lowBPM, int highBPM){
        if(lowBPM >= MIN_BPM && highBPM <= MAX_BPM)
            atc.initialize(lowBPM,highBPM);
    }
    // il metodo controlla che non ci siano altri suoni in esecuzione, in caso contrario chiama il metodo di AudioTrackController
    // se c'è già qualcosa in esecuzione ignora la chiamata

    /**
     * il metodo controlla che non ci siano altri suoni in esecuzione, in caso contrario chiama il metodo di AudioTrackController
     * se c'è già qualcosa in esecuzione ignora la chiamata
     */
    public void play(){
        if(!isPlaying){
            Log.v(LOG_TAG, "in play");
            atc.play();
            isPlaying = true;
        }
    }
    // il metodo controlla che ci sia un altro suono in esecuzione, in caso contrario ignora la chiamata
    public void stop(){
        if(isPlaying) {
            Log.v(LOG_TAG, "in stop");
            atc.stop();
            isPlaying = false;
        }
    }
    /*
        @param il nuovo numero di BPM
        controllo che il numero stia dentro al range massimo
     */
    public void setBPM(int BPM){
        if(BPM >= MIN_BPM || BPM <= MAX_BPM) {
            Log.v(LOG_TAG, "in setBPM");
            atc.setBPM(BPM);
        }

    }
    @Override

    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");
        return mBinder;
        // TODO: Return the communication channel to the service. DONE!!
    }



    public class myBinder extends Binder{
        SoundManagerService getService() {
            return SoundManagerService.this;
        }
    }
}
