package a2016.soft.ing.unipd.metronomepro;

import android.content.Context;
import android.view.View;

/**
 * Created by feder on 22/10/2016.
 */

public class SoundThread extends Thread implements View.OnClickListener {

    private static final long sensibilityInMs = 1;
    private static final long sensibility = 1000000;
    private long stepMillis;
    private SoundManager soundManager;
    private long lastNanoTime;
    private long nanoStep;
    private boolean run;

    public SoundThread(Context c) {
        soundManager = new SoundManager(c);
        setStepMillis(Long.MAX_VALUE);
    }
    public SoundThread(Context c, long stepMillis) {
        this(c);
        setStepMillis(stepMillis);
    }

    public SoundThread(Context c, int bPM) {
        this(c);
        setStepMillis(millisIntervalFromBPM(bPM));
    }

    /**
     * @param bPM battiti per minuto
     * @return parte bassa del tempo in millisecondi tra un minuto e l'altro
     */
    public static long millisIntervalFromBPM(int bPM) {
        return 60000 / bPM;
    }

    public long getStepMillis() {
        return stepMillis;
    }

    public void setStepMillis(long stepMillis) {
        nanoStep = stepMillis * 1000000;
        this.stepMillis = stepMillis;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }


    @Override
    public void run() {
        run = true;
        while (run) {
            soundManager.soundStart();
            //System.out.println(System.nanoTime()-lastNanoTime);
            try {
                lastNanoTime = System.nanoTime();
                long nextNanoTime = lastNanoTime + nanoStep - sensibility;
                while (System.nanoTime() <= nextNanoTime) {
                    Thread.sleep(sensibilityInMs);
                }
            } catch (InterruptedException ex) {
                run = false;
            }
        }
    }

    @Override
    public void onClick(View v) {
        //è sempre sicuro che sia solo lo start button ad usare questo metodo
//        if (v.getId() == R.id.start_button) {
            if (run) {
                run = false;
            } else {
                this.start();
            }

//        }
    }
}
