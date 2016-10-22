package a2016.soft.ing.unipd.metronomepro;

import android.content.Context;

/**
 * Created by feder on 22/10/2016.
 */

public class SoundThread extends Thread {

    private long stepMillis;
    private SoundManager soundManager;
    private boolean run;

    public SoundThread(Context c) {
        soundManager = new SoundManager(c);
        stepMillis = Long.MAX_VALUE;
    }

    public SoundThread(Context c, long stepMillis) {
        this(c);
        this.stepMillis = stepMillis;
    }

    public SoundThread(Context c, int bPM) {
        this(c);
        this.stepMillis = millisIntervalFromBPM(bPM);
    }

    /**
     * @param bPM battiti per minuto
     * @return parte bassa del tempo in millisecondi tra un minuto e l'altro
     */
    public static long millisIntervalFromBPM(int bPM) {
        return 0;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    @Override
    public void run() {
        while (run) {
            try {

                Thread.sleep(stepMillis);
            } catch (InterruptedException ex) {
                run = false;
            }
            soundManager.soundStart();
        }
    }
}
