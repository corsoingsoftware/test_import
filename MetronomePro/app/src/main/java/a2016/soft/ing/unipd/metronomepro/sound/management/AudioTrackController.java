package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
//prova di commit
/**
 * Created by feder on 12/11/2016.
 * Si occupa di gestire le interazioni con audioTrack, un'istanza di questa classe sarà contenuta nel service.
 * Sara lui che implementa completamente soundmanager e i suoi metodi non saranno più degli eco a qualcun altro ma
 * farà veramente qualcosa.
 *
 * Per code reviewer: Questa classe implementa l'interfaccia {@link SoundManager}
 * Serve a gestire un oggetto {@link AudioTrack} che si occuperà di riprodurre il suono
 * Lo scopo della classe è occuparsi di tutta la gestione audio, nascondendo a chi la usa le complicazioni della generazione della sinusoide
 * dell'accodamento del silenzio e della gestione dei loop
 * L'accesso è public non packagelocal perché secondo me è utile poterla usare da package differenti in futuro.
 */

public class AudioTrackController implements SoundManager {

    //private static final int WAV_HEADER_SIZE_IN_BYTES = 44;
    /**
     * Frequenza del campione audio
     */
    private static final int SAMPLE_RATE_IN_HERTZ = 8000;
    /**
     * Frequenza della sinusoide
     */
    private static final int SIN_FREQUENCY=610;
    /**
     * Grandezza del singolo frame audio in bytes nel nostro caso: n_byte_per_sample*channels=2*1 perché è un 16 bit mono
     */
    private static final int FRAME_SIZE = 2;
    /**
     * Lunghezza totale della sinusoide in bytes questa lunghezza deve essere molto più breve dei max bpm, che attualmente son 300
     * per decisione di struttura
     */
    private static final int SIN_LENGTH_IN_BYTES=500;
    /**
     * sample rate in hertz per numero di byte per sample per numero di canali
     */
    private int maxPeriodLengthInBytes;
    /**
     * Configurazione del canale audio
     */
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
    /**
     * Configurazione del formato: pcm è un formato non compresso, anche i wav han lo stesso coding
     */
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    /**
     * Numero massimo dei bpm impostabili
     */
    private int maxBPM;
    /**
     * Numero minimo dei BPM impostabili
     */
    private int minBPM;

    /**
     * Numero di BPM attuali
     */
    private int currBPM;
    /**
     * Lunghezza del buffer in bytes. essa è correlata alle impostazioni di canale
     */
    private int bufferSize;
    /**
     * lunghezza del buffer in frame, calcolata dividendo bufferSize per il numero di bytes per frame
     */
    private int bufferSizeInFrame;
    /**
     * Il suono effettivo (il beep) senza silenzio
     */
    private byte[] initialClack;
    /**
     * L'oggetto audiotrack che mi gestisce il silenzio
     */
    private AudioTrack at;

    /**
     * Getter for currBPM
     * @return currBPM
     */
    public int getCurrBPM() {
        return currBPM;
    }

    /**
     * Questo metodo carica i suoni iniziali, in futuro si prevedono due file audio a scelta, attualmente
     * i parametri son ignorati
     *
     * @param clack      il clack normale
     * @param clackFinal il clack di fine battuta
     */
    void loadFile(FileDescriptor clack, FileDescriptor clackFinal) {
        try {
            initialClack = new byte[SIN_LENGTH_IN_BYTES];
            //Generazione della sinusoide, non è stata fatta in un metodo separato in quanto è usata solo qui
            for(int i=0;i<SIN_LENGTH_IN_BYTES-1;i++){
                double b=Math.sin(2*Math.PI*i/(SAMPLE_RATE_IN_HERTZ/SIN_FREQUENCY));
                ByteBuffer bb = ByteBuffer.allocate(2);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                bb.putShort((short)(b*Short.MIN_VALUE));
                bb.position(0);
                initialClack[i++]=bb.get();
                initialClack[i]=bb.get();
            }

        } catch (Exception e) {
            e.printStackTrace();
            //non dovrebbe mai finire in questo blocco catch attualmente (ramo morto)
        }
    }

    /**
     * inizializza il suono tenendo conto del range di bpm.
     * In modo da ottimizzare il buffer in funzione di questi ultimi
     * @param minBPM numero minimo di BPM desiderati
     * @param maxBPM numero massimo di BPM desiderati
     */
    @Override
    public void initialize( int minBPM,int maxBPM) {
        this.maxBPM = maxBPM;
        this.minBPM = minBPM;
        maxPeriodLengthInBytes =(SAMPLE_RATE_IN_HERTZ * FRAME_SIZE *(60/minBPM));
        bufferSize = maxPeriodLengthInBytes;//android.media.AudioTrack.getMinBufferSize(SAMPLE_RATE_IN_HERTZ, CHANNEL_CONFIG, AUDIO_FORMAT);
        bufferSizeInFrame= bufferSize/FRAME_SIZE;
        at = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE_IN_HERTZ, CHANNEL_CONFIG,
                AUDIO_FORMAT, bufferSize, AudioTrack.MODE_STATIC);
        byte[] period = new byte[maxPeriodLengthInBytes];
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort(Short.MIN_VALUE);
        bb.position(0);
        byte first=bb.get();
        byte second=bb.get();
        for(int i=initialClack.length;i<period.length-1;i++){
            period[i++]=first;
            period[i]=second;
        }
        //il math.min serve nel caso il clack sia di lunghezza maggiore del periodo stesso, questo potrebbe essere un problema
        System.arraycopy(initialClack, 0, period, 0, Math.min(initialClack.length, period.length));
        at.write(period, 0, period.length);
        setBPM(minBPM);
    }

    /**
     * Fa partire il suono, deve impostare il loop correttamente (quando si stoppa il suono, le impostazioni loop vengono perse)
     * Guardare {@link AudioTrack}
     */
    @Override
    public void play() {
        at.play();
        setBPM(currBPM);
    }

    /**
     * Stoppa semplicemente il suono
     */
    @Override
    public void stop() {
        at.stop();
    }

    /**
     * Ritorna il playstate secondo le convenzioni da noi scelte
     * @return intero da 0 a 2: 0 stop, 1 play, 2 pause oppure 0 default
     */
    @Override
    public int getState() {
        switch(at.getPlayState()){
            case AudioTrack.PLAYSTATE_STOPPED:
                return 0;
            case AudioTrack.PLAYSTATE_PLAYING:
                return 1;
            case AudioTrack.PLAYSTATE_PAUSED:
                return 2;
            default: return -1; //Non dovrebbe mai essere qui.
        }
    }

    /**
     * Dovrà impostare la ritmica attualmente non ancora implementata
     * @param numerator numeratore della ritmica
     * @param denom denominatore
     */
    @Override
    public void setRhythmics(int numerator, int denom) {

    }

    /**
     * Cambia i bpm, capisce a che frame deve fermarsi
     * @param newBPM nuovo numero di BPM
     */
    @Override
    public void setBPM(int newBPM) {

        if(newBPM>=minBPM&&newBPM<=maxBPM) {
            currBPM = newBPM;
            boolean shouldRestart=false;
            if (at.getPlayState() != AudioTrack.PLAYSTATE_STOPPED) {
                at.stop();
                shouldRestart=true;
            }
            int frameStop=(int)Math.round((double)(60*bufferSizeInFrame)/(double)(currBPM*(60/minBPM)));
            //la riga sotto commentata serviva a capire l'errore in numero di frame
            //int error=((60*bufferSizeInFrame)%(currBPM*(60/minBPM)));
            at.setLoopPoints(0, frameStop, -1);
            if(shouldRestart) at.play();
        }

    }
}
