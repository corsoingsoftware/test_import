package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.ArrayList;
import java.util.HashMap;

import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;
import a2016.soft.ing.unipd.metronomepro.sound.management.generators.SignalsGenerator;

import static a2016.soft.ing.unipd.metronomepro.sound.management.PlayState.PLAYSTATE_PAUSE;
import static a2016.soft.ing.unipd.metronomepro.sound.management.PlayState.PLAYSTATE_PLAYING;
import static a2016.soft.ing.unipd.metronomepro.sound.management.PlayState.PLAYSTATE_STOP;
import static a2016.soft.ing.unipd.metronomepro.sound.management.PlayState.PLAYSTATE_UNKNOW;

/**
 * Created by Federico Favotto on 08/12/2016.
 * This class uses AudioTrack to create song in bytes from a Song, and provides methods to play the track!
 */
public class AudioTrackSongPlayer implements SongPlayer {

    static final int SAMPLE_RATE_IN_HERTZ = 8000;
    static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
    static final int FRAME_SIZE = 2;
    static final int SECS_IN_MIN = 60;
    /**
     * Frequenza della sinusoide
     * sin frequency of beep
     */
    static final int DEFAULT_BEEP_FREQUENCY=610;
    /**
     * Frequenza della sinusoide
     * sin frequency OF boop
     */
    static final int DEFAULT_BOOP_FREQUENCY=400;
    /**
     * Lunghezza totale della sinusoide in bytes questa lunghezza deve essere molto più breve dei max bpm, che attualmente son 300
     * per decisione di struttura
     * Total length in bytes of the "beep", it must be shorter than minimum period, so when bpm are higher
     */
    static final int DEFAULT_SIN_LENGTH_IN_BYTES=500;
    private static final String THREAD_NAME = "bufferT";
    private Thread writeAt;
    private boolean goThread;
    private AudioTrack at;
    /**
     * The current thread that's writing the buffer
     */
    private Thread currentThread;
    private boolean stop;
    /**
     * The name of song is key
     */
    private HashMap<String, byte[]> hashMap;
    private int frequencyBeep, lengthBeep, frequencyBoop, lenghtBoop;
    private SongPlayerCallback callback;

    public AudioTrackSongPlayer(SongPlayerCallback callback) {
        hashMap = new HashMap<String, byte[]>();
        this.initialize();
        stop = true;
        this.callback = callback;
    }

    public void play() {
        at.play();

    }

    /**
     * A lot of parameters but they are necessary, there are method with less parameters
     * @param frequencyBeep frequency of higher tone beep
     * @param lengthBeep length of higher tone beep
     * @param frequencyBoop frequency of lower tone boop
     * @param lengthBoop length of lower tone boop
     * @param sampleRate rate of audio signal
     * @param audioFormat format of audio signal
     * @param channelConfig config of channels
     */


    public void initialize(int frequencyBeep,
                           int lengthBeep,
                           int frequencyBoop,
                           int lengthBoop,
                           int sampleRate,
                           int audioFormat,
                           int channelConfig) {

        this.frequencyBeep = frequencyBeep;
        this.lengthBeep = lengthBeep;
        this.frequencyBoop = frequencyBoop;
        this.lenghtBoop = lengthBoop;

        at = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfig,
                audioFormat, AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat), AudioTrack.MODE_STREAM);


        goThread = true;
    }

    //richiama dal costruttore


    public void initialize(int sampleRate, int audioFormat, int channelConfig) {

        this.initialize(DEFAULT_BEEP_FREQUENCY,
                DEFAULT_SIN_LENGTH_IN_BYTES,
                DEFAULT_BOOP_FREQUENCY,
                DEFAULT_SIN_LENGTH_IN_BYTES,
                sampleRate,
                audioFormat,
                channelConfig);

    }

    public void initialize() {
        this.initialize(SAMPLE_RATE_IN_HERTZ, AUDIO_FORMAT, CHANNEL_CONFIG);
    }

    @Override
    public void pause() {
        at.pause();
    }

    @Override
    public void stop() {

        stop = true;
        if(currentThread!=null){
            if(!goThread){
                currentThread.interrupt();
            }
        }
        at.stop();

    }

    /**
     * Controlla se è già presente la song passata come parametro. Se non lo è la aggiungo all'Hash Map richiamando
     * getSong in modo da ottenere l'array che la rappresenta. Aggiungo il nome come chiave e l'array come valore.
     *
     * @param song canzone da aggiungere
     */

    @Override
    public void load(Song song) {


        if (!hashMap.containsKey(song.getName())) {

            //Create a new element for the hashMap

            hashMap.put(song.getName(), getSong(song));
        }

    }

    /**
     * Restituisce l'array che rappresenta la canzone. Costruisce la sinusoide e il silenzio utilizzando la classe SignalsGenerator.
     * @param s song to search
     * @return arraySong array di byte che rappresenta la canzone
     */

    @Override
    public byte[] getSong(Song s) {

        SignalsGenerator sGenerator = new SignalsGenerator();
        ArrayList<byte[]> listSong = new ArrayList<byte[]>();
        int numBytes = 0;

        /**
         *  Tutti gli elementi di listsong punteranno allo stesso array di bytes in, tanto dopo viene fatto un arraycopy.
         *  Risparmio memoria.
         */

        byte[] sound = sGenerator.generateSin(lengthBeep, frequencyBeep);

        TimeSlicesSong songSlices = (TimeSlicesSong) s;

        for (TimeSlice ts : songSlices) {

            int bpm_slice = ts.getBpm();
            int PeriodLengthInBytes = (int)((SAMPLE_RATE_IN_HERTZ * FRAME_SIZE * (SECS_IN_MIN / (double)bpm_slice))+0.5);
            if(PeriodLengthInBytes%2==1){
                //It can't be odd!!!!!
                PeriodLengthInBytes--;
            }

            byte[] silence = sGenerator.silence(PeriodLengthInBytes - lengthBeep);

            //Aggiungo arraySlice in coda alla lista che contiene tutti gli slices della canzone

            for (int count = 0; count < ts.getDurationInBeats(); count++) {
                listSong.add(sound);
                listSong.add(silence);
                numBytes += PeriodLengthInBytes;
            }
        }

        byte[] arraySong = new byte[numBytes];
        int indexOfArraySong = 0;

        for (byte[] sliceTemp : listSong) {

            System.arraycopy(sliceTemp, 0, arraySong, indexOfArraySong, sliceTemp.length);
            indexOfArraySong += sliceTemp.length;
        }

        return arraySong;
    }

    /**
     * Restituisce lo stato di AudioTrack.
     * @return Playstate identifica lo stato di AudioTrack
     */

    @Override
    public PlayState getState() {

        switch (at.getPlayState()) {
            case AudioTrack.PLAYSTATE_STOPPED:
                return PLAYSTATE_STOP;
            case AudioTrack.PLAYSTATE_PLAYING:
                return PLAYSTATE_PLAYING;
            case AudioTrack.PLAYSTATE_PAUSED:
                return PLAYSTATE_PAUSE;
            default:
                return PLAYSTATE_UNKNOW;
        }
    }

    /**
     * Riceve in input un array di Songs. Le cerca nell'HashMap e, se presenti, le scrive nel buffer di AudioTrack.
     * @param songs array contenente le canzoni
     * @throws Exception
     */
    public void write(final Song[] songs) {

        final SongPlayer sp = this;

        Runnable toDo= new Runnable() {
            @Override
            public void run() {

                byte[] arraySong;

                /*while (!goThread) {
                }  //Attesa!*/

                //Impedisco l'accesso al buffer da parte di altri Thread durante la scrittura
                goThread = false;
                stop = false;

                for (int i = 0; i < songs.length; i++) {

                    if (hashMap.containsKey(songs[i].getName())) {

                    arraySong = (byte[]) hashMap.get(songs[i].getName());
                        int indexWrite = 0;

                        while (indexWrite < arraySong.length) {

                            int bytesWritten = at.write(arraySong, indexWrite, arraySong.length - indexWrite);
                            indexWrite += bytesWritten;
                            if(stop)
                                return;
                        }
                    }
                }

                //Ho finito la scrittura nel buffer, consento l'accesso agli altri Thread
                goThread = true;
                callback.playEnded(sp);
            }
        };

        if(goThread){
            currentThread= new Thread(toDo,  THREAD_NAME);
            currentThread.start();
        }
    }

}
