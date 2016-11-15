package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;

import a2016.soft.ing.unipd.metronomepro.R;

import static a2016.soft.ing.unipd.metronomepro.sound.management.SoundServiceConstants.INITIAL_VALUE;
import static a2016.soft.ing.unipd.metronomepro.sound.management.SoundServiceConstants.MAX;
import static a2016.soft.ing.unipd.metronomepro.sound.management.SoundServiceConstants.MIN;

/**
 * Riceve le chiamate in entrata e gestisce Audiotrack!
 * Contiene al suo interno un audiotrackcontroller e lo gestisce correttamente in base alle chiamate che gli arrivano!!
 */
public class SoundManagerService extends Service {

    private static final String LOG_TAG = "SoundManagerService";       //aggiungo una stringa di log per facilitare il debug
    private IBinder mBinder = new myBinder();
    private AudioTrackController atc;
    private boolean isPlaying = false;

   /* public SoundManagerService() {

    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        this.atc = new AudioTrackController();

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
        this.atc.loadFile(afdClack.getFileDescriptor(),afdClackFinal.getFileDescriptor());
        this.atc.initialize(MIN,MAX);
        this.atc.setBPM(INITIAL_VALUE);
    }

    // i vari metodi ricevono i comandi da SoundManagerServiceCaller e dopo aver fatto i controlli chiamano i metodi
    //   di AudioTrackController


    /**
     *  controllo che il massimo e il minimo siano entro il range rispettato
     */
    public void initialize(int min,int max){
        atc.initialize(min,max);
    }

    /**
     * Numero bpm attualmente impostato
     * @return bpm
     */
    public int getBPM(){
        return atc.getCurrBPM();
    }

    /**
     * Stato del player
     * @return 0 none/stop 1 play 2 pause
     */
    public int getState() {
        return atc.getState();
    }
    // il metodo controlla che non ci siano altri suoni in esecuzione, in caso contrario chiama il metodo di AudioTrackController
    // se c'è già qualcosa in esecuzione ignora la chiamata

    /**
     * il metodo controlla che non ci siano altri suoni in esecuzione, in caso contrario chiama il metodo di AudioTrackController
     * se c'è già qualcosa in esecuzione ignora la chiamata
     */
    public void play(){
            atc.play();
    }
    // il metodo controlla che ci sia un altro suono in esecuzione, in caso contrario ignora la chiamata
    public void stop(){
            atc.stop();
    }
    /*
        @param il nuovo numero di BPM
        controllo che il numero stia dentro al range massimo
     */
    public void setBPM(int BPM){
        Log.v(LOG_TAG, "in setBPM");
        atc.setBPM(BPM);
    }
    @Override

    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");
        return mBinder;
    }



    public class myBinder extends Binder{
        SoundManagerService getService() {
            return SoundManagerService.this;
        }
    }
}
