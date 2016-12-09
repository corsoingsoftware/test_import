package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.ArrayList;
import java.util.HashMap;

import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.sound.management.generators.SignalsGenerator;

/**
 * Created by feder on 08/12/2016.
 * This class uses AudioTrack to create song in bytes from a Song, and provides methods to play the track!
 */

public class AudioTrackSongPlayer implements SongPlayer {

    private AudioTrack at;
    private HashMap hashMap;
    private int frequencyBeep, lengthBeep, frequencyBoop, lenghtBoop;

    public AudioTrackSongPlayer() {
        this.initialize();
    }

    @Override
    public void play() {

    }

    //richiama dal costruttore

    @Override
    public void initialize(int frequencyBeep, int lengthBeep, int frequencyBoop, int lengthBoop, int sampleRate, int audioFormat, int channelConfig) {

        this.frequencyBeep = frequencyBeep;
        this.lengthBeep = lengthBeep;
    }

    @Override
    public void initialize(int sampleRate, int audioFormat, int channelConfig) {

        this.initialize(0, 0, 0, 0, SAMPLE_RATE_IN_HERTZ, AUDIO_FORMAT, CHANNEL_CONFIG);
    }

    @Override
    public void initialize() {
        this.initialize(SAMPLE_RATE_IN_HERTZ, AUDIO_FORMAT, CHANNEL_CONFIG);
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void load(Song song) {

        /* Gli arriva una song in ingresso, controllo se è già presente. Se non lo è, la aggiungo all'Hash Map richiamando
            getSong per ottenere l'array. Aggiungo il nome come chiave e l'array come valore.
        */

        if (!hashMap.containsKey(song.getName())) {

            //Create a new element for the hashtable

            hashMap.put(song.getName(), getSong(song));
        }

    }

    @Override
    public byte[] getSong(Song s) {

        //Restituisce l'array costruito a partire dalla canzone (composta da più Time Slice). Costruisco le sinusoidi ed etc.

        SignalsGenerator sGenerator = new SignalsGenerator();
        ArrayList<byte[]> listSong = new ArrayList<byte[]>();
        //int num_slices = 0;

        for (TimeSlice ts : s) {

            int bpm_slice = ts.getBpm();
            int PeriodLengthInBytes = (SAMPLE_RATE_IN_HERTZ * FRAME_SIZE * (SECS_IN_MIN / bpm_slice));

            byte[] sound = sGenerator.generateSin(lengthBeep, frequencyBeep);
            byte[] silence = sGenerator.silence(PeriodLengthInBytes - lengthBeep);

            byte[] arraySlice = new byte[sound.length + silence.length];

            //Concateno i due array

            System.arraycopy(sound, 0, arraySlice, 0, sound.length);
            System.arraycopy(silence, 0, arraySlice, sound.length - 1, silence.length);

            //Aggiungo arraySlice in coda alla lista che contiene tutti gli slices della canzone

            listSong.add(arraySlice);
            //num_slices++;
        }


        byte[] arraySong = new byte[0];
        byte[] arrayTemp;

        for (byte[] sliceTemp : listSong) {

            arrayTemp = new byte[arraySong.length + sliceTemp.length];
            System.arraycopy(arraySong, 0, arrayTemp, 0, arraySong.length);
            System.arraycopy(sliceTemp, 0, arrayTemp, arraySong.length - 1, sliceTemp.length);
            arraySong = arrayTemp;
        }

        return arraySong;
    }

    @Override
    public PlayState getState() {
        return null;
    }

    public void write(Song[] songs) {

        // Riceve in input un array di Songs. Cerca nell'HashMap, se le trova le scrive nel buffer di AudioTrack.
        byte[] arraySong;

        at = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE_IN_HERTZ, CHANNEL_CONFIG,
                AUDIO_FORMAT, AudioTrack.getMinBufferSize(SAMPLE_RATE_IN_HERTZ, CHANNEL_CONFIG, AUDIO_FORMAT), AudioTrack.MODE_STATIC);

        for (int i = 0; i < songs.length; i++) {

            if (hashMap.containsValue(songs[i].getName())) {

                arraySong = (byte[]) hashMap.get(songs[i].getName());
                at.write(arraySong, 0, arraySong.length);
            }
        }
    }
}
