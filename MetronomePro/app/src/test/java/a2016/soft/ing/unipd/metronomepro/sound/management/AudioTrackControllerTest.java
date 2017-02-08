package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import org.junit.Test;
import a2016.soft.ing.unipd.metronomepro.MetronomeActivity;
import static org.junit.Assert.*;

/**
 * Created by Francesco on 03/12/2016.
 */
public class AudioTrackControllerTest {
    @Test
    public void test() {
        Context c;
        c = new MockContext();

    }
    public void testSetBpm() {
        AudioTrackController at = new AudioTrackController();
        at.initialize(30, 300);
        at.setBPM(400);
        at.setBPM(10);
    }

         }

