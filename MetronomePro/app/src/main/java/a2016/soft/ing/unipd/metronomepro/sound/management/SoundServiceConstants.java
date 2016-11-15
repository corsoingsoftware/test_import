package a2016.soft.ing.unipd.metronomepro.sound.management;

/**
 * Created by Francesc on 12/11/2016.
 *
 * In questa iinterfaccia racoolgo le variabili per la comunicazione tra il @SoundManagerServiceCaller
 * e il @SoundManagerService
 */

public interface SoundServiceConstants {

    public final static int STATE_INITIALIZED = 0;
    public final static boolean STATE_PLAYING = false;
    public final static boolean STATE_STOP = true;
    public final static int PLAY = 3;
    public final static int STOP = 4;
    public final static int STATE_SETTING_BPM = 5;

    public static final int MIN=30, MAX=300, INITIAL_VALUE=120;

}
