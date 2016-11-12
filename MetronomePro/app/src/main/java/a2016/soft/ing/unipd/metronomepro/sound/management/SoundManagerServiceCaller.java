package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.content.Context;

import a2016.soft.ing.unipd.metronomepro.*;

/**
 * Created by feder on 12/11/2016.
 * L'activity avrà un'istanza di questa classe che si occuperà a chiamare il servizio!!
 * Le chiamate dell'interfaccia SoundManager non saranno altro che degli ECO sul servizio!
 */

public class SoundManagerServiceCaller implements SoundManager {

    public SoundManagerServiceCaller(Context c){

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
