package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.sound.management.generators.SignalsGenerator;

import static a2016.soft.ing.unipd.metronomepro.sound.management.PlayState.PLAYSTATE_PAUSE;
import static a2016.soft.ing.unipd.metronomepro.sound.management.PlayState.PLAYSTATE_PLAYING;
import static a2016.soft.ing.unipd.metronomepro.sound.management.PlayState.PLAYSTATE_STOP;
import static a2016.soft.ing.unipd.metronomepro.sound.management.PlayState.PLAYSTATE_UNKNOW;

/**
 * Created by feder on 08/12/2016.
 * This class uses AudioTrack to create song in bytes from a Song, and provides methods to play the track!
 */

public class AudioTrackSongPlayer implements SongPlayer {

    private AudioTrack at;
    /**
     * The name of song is key
     */
    private HashMap<String, byte[]> hashMap;
    private int frequencyBeep, lengthBeep, frequencyBoop, lenghtBoop;

    public AudioTrackSongPlayer() {
        hashMap = new HashMap<String, byte[]>();
        this.initialize();
    }

    @Override
    public void play() {
        at.play();
    }

    //richiama dal costruttore

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
    @Override
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
                audioFormat, AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat), AudioTrack.MODE_STATIC);

    }

    @Override
    public void initialize(int sampleRate, int audioFormat, int channelConfig) {

        this.initialize(DEFAULT_BEEP_FREQUENCY,
                DEFAULT_SIN_LENGTH_IN_BYTES,
                DEFAULT_BOOP_FREQUENCY,
                DEFAULT_SIN_LENGTH_IN_BYTES,
                sampleRate,
                audioFormat,
                channelConfig);

    }

    @Override
    public void initialize() {
        this.initialize(SAMPLE_RATE_IN_HERTZ, AUDIO_FORMAT, CHANNEL_CONFIG);
    }

    @Override
    public void pause() {
        at.pause();
    }

    @Override
    public void stop() {

        at.stop();
    }

    @Override
    public void load(Song song) {

        /* Arriva una song in ingresso, controllo se è già presente. Se non lo è, la aggiungo all'Hash Map richiamando
            getSong per ottenere l'array. Aggiungo il nome come chiave e l'array come valore.
        */

        if (!hashMap.containsKey(song.getName())) {

            //Create a new element for the hashMap

            hashMap.put(song.getName(), getSong(song));
        }

    }

    @Override
    public byte[] getSong(Song s) {

        //Restituisce l'array costruito a partire dalla canzone (composta da più Time Slice). Costruisco le sinusoidi ed etc.

        SignalsGenerator sGenerator = new SignalsGenerator();
        ArrayList<byte[]> listSong = new ArrayList<byte[]>();
        int numBytes=0;

        //Tutti gli elementi di listsong punteranno allo stesso array di bytes
        //ma tanto dopo viene fatto un arraycopy
        //risparmio memoria
        byte[] sound = sGenerator.generateSin(lengthBeep, frequencyBeep);
        for (TimeSlice ts : s) {

            int bpm_slice = ts.getBpm();
            int PeriodLengthInBytes = (SAMPLE_RATE_IN_HERTZ * FRAME_SIZE * (SECS_IN_MIN / bpm_slice));

            byte[] silence = sGenerator.silence(PeriodLengthInBytes - lengthBeep);
            numBytes+=PeriodLengthInBytes;

            //Aggiungo arraySlice in coda alla lista che contiene tutti gli slices della canzone
            listSong.add(sound);
            listSong.add(silence);
        }
        byte[] arraySong = new byte[numBytes];
        int indexOfArraySong=0;
        for (byte[] sliceTemp : listSong) {
            System.arraycopy(sliceTemp,0,arraySong,indexOfArraySong,sliceTemp.length);
            indexOfArraySong+=sliceTemp.length;
        }
        return arraySong;
    }

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

    public void write(Song[] songs) throws Exception {

        // Riceve in input un array di Songs. Cerca nell'HashMap, se le trova le scrive nel buffer di AudioTrack.
        byte[] arraySong;


        for (int i = 0; i < songs.length; i++) {

            if (hashMap.containsKey(songs[i].getName())) {
                arraySong = hashMap.get(songs[i].getName());
                int bytesWritten=at.write(arraySong, 0, arraySong.length);
                //just to know how to works, to the first test.
                if(bytesWritten<arraySong.length) throw new Exception("Dobbiamo mettere il Thread");
            }
        }
    }
}
