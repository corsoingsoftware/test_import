package a2016.soft.ing.unipd.metronomepro.sound.management;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by feder on 12/11/2016.
 * Si occupa di gestire le interazioni con audioTrack, un'istanza di questa classe sarà contenuta nel service.
 * Sara lui che implementa completamente soundmanager e i suoi metodi non saranno più degli eco a qualcun altro ma
 * farà veramente qualcosa.
 */

public class AudioTrackController implements SoundManager {

    private byte[] initialClack; //metto i due array come variabili per potervi accedere da fuori
    private byte[] finalClack;
    /**
     * Questo metodo carica questi due file in due array, per poi poterli gestire
     * @param clack il clack normale
     * @param clackFinal il clack di fine battuta
     */
    void loadFile(FileDescriptor clack, FileDescriptor clackFinal) {
        //inizializzo i due array di byte
        try {
            FileInputStream streamInClack = new FileInputStream(clack);
            FileInputStream streamFinClack = new FileInputStream(clackFinal);
            initialClack = new byte[streamInClack.available()];
            finalClack = new byte[streamFinClack.available()];
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
