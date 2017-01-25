package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.os.IBinder;

/**
 * Created by Federico Favotto on 12/11/2016.
 * Questa interfaccia espone una serie di metodi utili per far partire e gestire il "clack" del metronomo
 *
 * It provides methods to manage the sound with a particular bpm.
 * It provides also a group of utility constants, so the class that uses the interface is independent from the class that
 * implements the interface
 */

public interface SoundManager {

     /**
     * Questo metodo inizializza le variabili interne necessarie alla classe per poter gestire gli altri metodi.
     * Esso va sempre chiamato all'inizializzazione della classe che implementa l'interfaccia
     * se i parametri maxbpm e minbpm son impostati e diversi da -1 allora la classe implementatrice cercherà di ottimizzare
     * le sue funzionalità per garantire un funzionamento più affidabile nell'intervallo.
     * (advice per chi implementerà: da questi si può trovare un buffer size ottimale e divisibile per questi, in modo da trovare l'unità di
     * frame corretta e sapere ogni quanto tempo viene perso un frame, inoltre precaricherà un suono adeguato per massimizzare le prestazioni
     * successivamente)
     * @param minBPM numero minimo di BPM desiderati
     * @param maxBPM numero massimo di BPM desiderati
     */
    void initialize(int minBPM,int maxBPM);

    /**
     * Questo metodo fa partire il suono correntemente impostato, se esso è già in esecuzione allora non fa nulla
     */
    void play();

    /**
     * Ferma il suono correntemente impostato. Se esso è fermo allora non fa nulla
     */
    void stop();

    /**
     * restituisce lo stato attuale del player
     * @return playstate that describes status
     */
    PlayState getState();

    /**
     * Imposta la ritmica, se il suono è in esecuzione allora viene fermato.
     * @param numerator numeratore della ritmica
     * @param denom denominatore
     */
    void setRhythmics(int numerator, int denom);

    /**
     * Imposta il numero di BPM attuali, se il suono è in esecuzione allora esso viene stoppato!
     * @param newBPM nuovo numero di BPM
     */
    void setBPM(int newBPM);
}
