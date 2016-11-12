package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Riceve le chiamate in entrata e gestisce Audiotrack!
 * Contiene al suo interno un audiotrackcontroller e lo gestisce correttamente in base alle chiamate che gli arrivano!!
 */
public class SoundManagerService extends Service {

    private final IBinder mBinder = new LocalBinder();
    private AudioTrackController AT = new AudioTrackController();

    public SoundManagerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");

        return mBinder;
    }

    public void play() {
        AT.play();
    }

    //Metodi per il client

    public class LocalBinder extends Binder {

        SoundManagerService getService() {
            return SoundManagerService.this;
        }
    }


}
