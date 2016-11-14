package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by feder on 12/11/2016.
 * L'activity avrà un'istanza di questa classe che si occuperà a chiamare il servizio!!
 * Le chiamate dell'interfaccia SoundManager non saranno altro che degli ECO sul servizio!
 */

public class SoundManagerServiceCaller implements SoundManager {

    private SoundManagerService mService;
    private Context cService;
    private boolean mBound;     //Flag che indica se è stato chiamato il bind sul servizio

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            // We've bound to SoundManagerService, cast the IBinder and get LocalService instance
            SoundManagerService.myBinder binder = (SoundManagerService.myBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public SoundManagerServiceCaller(Context c) {
        cService = c;
    }

    @Override
    public void initialize(int maxBPM, int minBPM) {

        //Creo il Bundle (Intent), aggiunti i parametri e avvio il servizio con il Bundle
        //Le stringhe vanno memorizzate nello stesso posto

        Intent intent = new Intent(cService, SoundManagerService.class);
        cService.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void play() {
        mService.play();
    }

    @Override
    public void stop() {
        mService.stop();
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public void setRhythmics(int numerator, int denom) {

    }

    @Override
    public void setBPM(int newBPM) {
        mService.setBPM(newBPM);
    }
}
