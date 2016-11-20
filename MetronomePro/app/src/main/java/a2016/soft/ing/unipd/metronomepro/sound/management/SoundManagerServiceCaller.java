package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import a2016.soft.ing.unipd.metronomepro.MetronomeActivity;

/**
 * L'activity avrà un'istanza di questa classe che si occuperà di chiamare il servizio.
 * Le chiamate dell'interfaccia SoundManager non saranno altro che degli ECHO sul servizio.
 */

public class SoundManagerServiceCaller implements SoundManager {

    private SoundManagerService mService;
    private MetronomeActivity activityContext;
    private boolean mBound;     //Booleano che indica se è stata effettuata o meno la connessione al servizio.
    private ServiceConnection mConnection;


    /**
     * Costruttore della classe.
     * All'interno di esso avviene l'effettiva connessione.
     *
     * @param c (Istanza di MetronomeActivty dalla quale parte la richiesta di connessione.)
     */

    public SoundManagerServiceCaller(MetronomeActivity c) {

        //Ottengo il context in modo da poter utilizzare i metodi di MetronomeActivity e creare i pacchetti.
        activityContext = c;

        //Creo un oggetto di tipo ServiceConnection necessario all'avvio del servizio e sovrascrivo i suoi metodi per
        //gestire le fasi di connessione e disconnessione.

        mConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {

                /*
                    Effettuo il cast a myBinder. (Classe interna estensiva di IBinder dichiarata in SoundManagerService)
                    Poichè il servizio è utilizzato esclusivamente dall'applicazione e non deve cooperare con altri processi, la scelta
                    migliore è quella di creare di un proprio Binder che permetta l'accesso ai metodi pubblici del servizio.
                */
                SoundManagerService.myBinder binder = (SoundManagerService.myBinder) service;

                /*
                    Ottengo l'istanza del servizio locale attraverso la chiamata del metodo getService.
                    Posso ora richiamare i metodi pubblici del servizio.
                */

                mService = binder.getService();
                mBound = true;

                //Chiamata al metodo onServiceInitialized di MetronomeActivity.
                activityContext.onServiceInitialized();
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                mBound = false;
            }
        };

        //Creo il pacchetto Intent che conterrà il context e la classe del servizio.
        Intent intent = new Intent(activityContext, SoundManagerService.class);

        //Connessione.
        activityContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * @param minBPM numero minimo di BPM desiderati.
     * @param maxBPM numero massimo di BPM desiderati.
     */

    @Override
    public void initialize(int minBPM, int maxBPM) {
        mService.initialize(minBPM,maxBPM);
    }

    /**
     * @return int BPM attuali.
     */

    public int getBPM(){
        return mService.getBPM();
    }

    /**
     * @param newBPM nuovo numero di BPM.
     */

    @Override
    public void setBPM(int newBPM) {
        mService.setBPM(newBPM);
    }

    /**
     * @return int Stato del servizio in esecuzione.
     */

    @Override
    public int getState() {
        return mService.getState();
    }

    @Override
    public void setRhythmics(int numerator, int denom) {
        //Da completare
    }

    //Metodi che si occupano delle chiamate ECHO sul servizio.

    @Override
    public void play() {
        mService.play();
    }

    @Override
    public void stop() {
        mService.stop();
    }
}
