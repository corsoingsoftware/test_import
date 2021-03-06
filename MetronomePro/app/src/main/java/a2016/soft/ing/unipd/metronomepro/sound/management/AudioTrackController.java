package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.FileDescriptor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static a2016.soft.ing.unipd.metronomepro.sound.management.PlayState.PLAYSTATE_PAUSE;
import static a2016.soft.ing.unipd.metronomepro.sound.management.PlayState.PLAYSTATE_PLAYING;
import static a2016.soft.ing.unipd.metronomepro.sound.management.PlayState.PLAYSTATE_STOP;
import static a2016.soft.ing.unipd.metronomepro.sound.management.PlayState.PLAYSTATE_UNKNOW;

/**
 * Created by Federico Favotto on 12/11/2016.
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
     * Audio frequency
     */
    private static final int SAMPLE_RATE_IN_HERTZ = 8000;
    /**
     * Seconds in a minute
     */
    private static final int SECS_IN_MIN=60;
    /**
     * Frequenza della sinusoide
     * sin frequency
     */
    private static final int SIN_FREQUENCY=610;
    /**
     * size of a single frame in bytes, in our case it is n_byte_per_sample*channels=2*1 because it's a 16bit mono track
     * Grandezza del singolo frame audio in bytes nel nostro caso: n_byte_per_sample*channels=2*1 perché è un 16 bit mono
     */
    private static final int FRAME_SIZE = 2;
    /**
     * Lunghezza totale della sinusoide in bytes questa lunghezza deve essere molto più breve dei max bpm, che attualmente son 300
     * per decisione di struttura
     * Total length in bytes of the "beep", it must be shorter than minimum period, so when bpm are higher
     */
    private static final int SIN_LENGTH_IN_BYTES=500;
    /**
     * sample rate in hertz per numero di byte per sample per numero di canali
     * sample_rate_in_hz * number of bytes per sample * channels count
     */
    private int maxPeriodLengthInBytes;
    /**
     * Configurazione del canale audio
     * Config for Audiotrack
     */
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
    /**
     * Configurazione del formato: pcm è un formato non compresso, anche i wav han lo stesso coding
     * Config format for audiotrack
     */
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    /**
     * Numero massimo dei bpm impostabili
     * Max number of bpm
     */
    private int maxBPM;
    /**
     * Numero minimo dei BPM impostabili
     * min number of bpm
     */
    private int minBPM;

    /**
     * Numero di BPM attuali
     * actual number of bpm
     */
    private int currBPM;
    /**
     * Lunghezza del buffer in bytes. essa è correlata alle impostazioni di canale
     * buffer size in bytes
     */
    private int bufferSize;
    /**
     * lunghezza del buffer in frame, calcolata dividendo bufferSize per il numero di bytes per frame
     * buffer size in frame to set loop points
     */
    private int bufferSizeInFrame;
    /**
     * Il suono effettivo (il beep) senza silenzio
     * the sin of "beep"
     */
    private byte[] initialClack;
    /**
     * L'oggetto audiotrack che mi gestisce il silenzio
     * Audiotrack object
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
    public void loadFile(FileDescriptor clack, FileDescriptor clackFinal) {
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
     * It initialize the class optimizing for min-max interval
     * @param minBPM numero minimo di BPM desiderati
     * @param maxBPM numero massimo di BPM desiderati
     */
    @Override
    public void initialize( int minBPM,int maxBPM) {
        this.maxBPM = maxBPM;
        this.minBPM = minBPM;
        maxPeriodLengthInBytes =(SAMPLE_RATE_IN_HERTZ * FRAME_SIZE *(SECS_IN_MIN/minBPM));
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
     * play sound with AudioTrack
     */
    @Override
    public void play() {
        at.play();
        setBPM(currBPM);
    }

    /**
     * Stoppa semplicemente il suono
     * Stop the sound
     */
    @Override
    public void stop() {
        at.stop();
    }

    /**
     * Ritorna il playstate secondo le convenzioni da noi scelte
     * Return playstate with playstateconstant @{@link SoundManager}
     * @return playstate defined by constants, default= playstatte_unknow
     */
    @Override
    public PlayState getState() {
        switch(at.getPlayState()){
            case AudioTrack.PLAYSTATE_STOPPED:
                return PLAYSTATE_STOP;
            case AudioTrack.PLAYSTATE_PLAYING:
                return PLAYSTATE_PLAYING;
            case AudioTrack.PLAYSTATE_PAUSED:
                return PLAYSTATE_PAUSE;
            default: return PLAYSTATE_UNKNOW; //Non dovrebbe mai essere qui.
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
     * Change current bpm, change loop points to satisfy new bpm request
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
            int frameStop=(int)Math.round((double)(SECS_IN_MIN*bufferSizeInFrame)/(double)(currBPM*(SECS_IN_MIN/minBPM)));
            //la riga sotto commentata serviva a capire l'errore in numero di frame
            //int error=((60*bufferSizeInFrame)%(currBPM*(60/minBPM)));
            at.setLoopPoints(0, frameStop, -1);
            if(shouldRestart) at.play();
        }

    }

    public AudioTrack getAudioTrack(){
        return at;
    }
}
