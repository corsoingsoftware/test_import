package a2016.soft.ing.unipd.metronomepro.sound.management;

import java.io.FileDescriptor;

import a2016.soft.ing.unipd.metronomepro.*;

/**
 * Created by feder on 12/11/2016.
 * Si occupa di gestire le interazioni con audioTrack, un'istanza di questa classe sarà contenuta nel service.
 * Sara lui che implementa completamente soundmanager e i suoi metodi non saranno più degli eco a qualcun altro ma
 * farà veramente qualcosa.
 */

public class AudioTrackController implements SoundManager {

    /**
     * Questo metodo carica questi due file in due array, per poi poterli gestire
     * @param clack il clack normale
     * @param clackFinal il clack di fine battuta
     */
    void loadFile(FileDescriptor clack, FileDescriptor clackFinal){

    }

    @Override
    public void initialize(int maxBPM, int minBPM) {

    }

    @Override
    public void play() {

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
