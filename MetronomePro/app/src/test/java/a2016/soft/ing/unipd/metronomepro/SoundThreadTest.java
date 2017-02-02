package a2016.soft.ing.unipd.metronomepro;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Federico Favotto on 11/11/2016.
 */
public class SoundThreadTest {
    @Test
    public void millisIntervalFromBPM() throws Exception {
        assertEquals(1000,SoundThread.millisIntervalFromBPM(60));
    }

    @Test
    public void setStepMillis() throws Exception {

    }



}