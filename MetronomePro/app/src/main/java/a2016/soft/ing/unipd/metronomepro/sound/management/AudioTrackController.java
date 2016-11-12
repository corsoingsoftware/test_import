package a2016.soft.ing.unipd.metronomepro.sound.management;

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
    private byte[] initialClack; //metto i due array come variabili per potervi accedere da fuori
    private byte[] finalClack;
    private ByteBuffer silence = ByteBuffer.allocate(2);

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
            byte[] initialClack1 = new byte[streamInClack.available()];
            byte[] finalClack1 = new byte[streamFinClack.available()];

            //inizializzo il byte di silenzio
            silence.putShort(Short.MIN_VALUE);
            //TODO: accodare questo buffer ad entrambi gli arrey di byte
            //adesso tolgo gli header di 44 byte all'inizio dei file
            initialClack = new byte[initialClack1.length - WAV_HEADER_SIZE_IN_BYTES];
            finalClack = new byte[finalClack1.length - WAV_HEADER_SIZE_IN_BYTES];
            //legge i file e li salva nei due array
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

            //mi trovo con due array di dimensioni clack -44Byte
            //adesso li riempio
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
        } catch (IOException e) {
        }
    }

    @Override
    public void initialize(int maxBPM, int minBPM) {

    }

    @Override
    public void play() {
//audio track = audiotrack.play();
    }

    @Override
    public void stop() {

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

    }
}
