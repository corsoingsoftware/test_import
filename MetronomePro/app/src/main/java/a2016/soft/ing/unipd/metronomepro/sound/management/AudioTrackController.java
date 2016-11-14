package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by feder on 12/11/2016.
 * Si occupa di gestire le interazioni con audioTrack, un'istanza di questa classe sarà contenuta nel service.
 * Sara lui che implementa completamente soundmanager e i suoi metodi non saranno più degli eco a qualcun altro ma
 * farà veramente qualcosa.
 */

public class AudioTrackController implements SoundManager {

    private static final int WAV_HEADER_SIZE_IN_BYTES = 44;
    private static final int SAMPLE_RATE_IN_HERTZ = 44100;
    private static int maxBPM;
    private static int minBPM;
    private static int currBPM;
    private static boolean isRun = false;
    private byte[] initialClack; //metto i due array come variabili per potervi accedere da fuori
    private byte[] finalClack;
    private ByteBuffer silence = ByteBuffer.allocate(2);
    private AudioTrack at;
    /**
     * Questo metodo carica questi due file in due array, per poi poterli gestire
     * @param clack il clack normale
     * @param clackFinal il clack di fine battuta
     */
    void loadFile(FileDescriptor clack, FileDescriptor clackFinal) {
        try {
            //inizializzo i due array di byte
            FileInputStream streamInClack = new FileInputStream(clack);
            FileInputStream streamFinClack = new FileInputStream(clackFinal);
            //array temporanei
            byte[] initialClack1 = new byte[streamInClack.available()];
            byte[] finalClack1 = new byte[streamFinClack.available()];

            //inizializzo il byte di silenzio
            silence.putShort(Short.MIN_VALUE);
            //adesso tolgo gli header di 44 byte all'inizio dei file
            //inizializzo i due array al doppio della dimensione del suono
            initialClack = new byte[initialClack1.length * 2 - WAV_HEADER_SIZE_IN_BYTES];
            finalClack = new byte[finalClack1.length * 2 - WAV_HEADER_SIZE_IN_BYTES];
            //legge i file e li salva nei due array temoranei
            try {
                streamInClack.read(initialClack1);
                streamFinClack.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                streamFinClack.read(finalClack1);
                streamFinClack.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //mi trovo con due array provvisori riepiti col suono
            //adesso tolgo gli header e salvo il suono nei due array difinitivi
            for (int i = WAV_HEADER_SIZE_IN_BYTES - 1; i < initialClack1.length; i++) {
                int j = 0;
                initialClack[j] = initialClack1[i];
                j = j + 1;
            }
            for (int i = WAV_HEADER_SIZE_IN_BYTES - 1; i < finalClack1.length; i++) {
                int j = 0;
                finalClack[j] = finalClack1[i];
                j = j + 1;
            }
            //accodo il silenzio nei due array
            for (int i = finalClack1.length - WAV_HEADER_SIZE_IN_BYTES; i < finalClack.length; i++) {
                finalClack[i] = silence.get();
            }
            for (int i = initialClack1.length - WAV_HEADER_SIZE_IN_BYTES; i < initialClack.length; i++) {
                initialClack[i] = silence.get();
            }
            //a questo punto mi trovo con due array con i rispettivi suoni senza gli header con accodati i silenzi!
        } catch (IOException e) {
        }
    }

    @Override
    public void initialize(int maxBPM, int minBPM) {
        AudioTrackController.maxBPM = maxBPM;
        AudioTrackController.minBPM = minBPM;
    }

    @Override
    public void play() {
//audio track = audiotrack.play();
        //inizializzo AudioTrack, COPIATO SPUTATO DA FEDERICO: SOUNDMANAGER
        int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int intSize = android.media.AudioTrack.getMinBufferSize(SAMPLE_RATE_IN_HERTZ, channelConfig, audioFormat);
        at = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE_IN_HERTZ, channelConfig,
                audioFormat, intSize, AudioTrack.MODE_STATIC);
        //scrivo un array di byte in AudioTrack
        at.write(initialClack, 0, initialClack.length);
        at.play();
        //TODO: gli array sono due! bisogna fare due play??
    }

    @Override
    public void stop() {
        at.stop();
    }

    @Override
    public int getState() {
        if (isRun == true)
            return 1;
        else
            return 0;
    }

    @Override
    public void setRhythmics(int numerator, int denom) {

    }

    @Override
    public void setBPM(int newBPM) {
        currBPM = newBPM;
    }
}
