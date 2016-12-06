package a2016.soft.ing.unipd.metronomepro;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioTrack;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import a2016.soft.ing.unipd.metronomepro.sound.management.AudioTrackController;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void setBpm() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        AudioTrackController atc = new AudioTrackController();
        AssetFileDescriptor afdClack = appContext.getAssets().openFd(appContext.getString(R.string.fileAudioName));
        AssetFileDescriptor afdClackFinal = appContext.getAssets().openFd(appContext.getString(R.string.fileAudioName));
        atc.loadFile(afdClack.getFileDescriptor(), afdClackFinal.getFileDescriptor());
        atc.initialize(30, 300);

        atc.setBPM(100);
        atc.setBPM(400);
        assertNotEquals(400, atc.getCurrBPM());
    }

    @Test
    public void getState() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        AudioTrackController atc = new AudioTrackController();
        AssetFileDescriptor afdClack = appContext.getAssets().openFd(appContext.getString(R.string.fileAudioName));
        AssetFileDescriptor afdClackFinal = appContext.getAssets().openFd(appContext.getString(R.string.fileAudioName));
        atc.loadFile(afdClack.getFileDescriptor(), afdClackFinal.getFileDescriptor());
        atc.initialize(30,300);

        assertEquals(0, atc.getState());
        atc.play();
        assertEquals(1, atc.getState());
        atc.stop();
        assertEquals(0, atc.getState());
    }
    @Test
    public void play() throws Exception{
        Context appContext = InstrumentationRegistry.getTargetContext();
        AudioTrackController atc = new AudioTrackController();
        AssetFileDescriptor afdClack = appContext.getAssets().openFd(appContext.getString(R.string.fileAudioName));
        AssetFileDescriptor afdClackFinal = appContext.getAssets().openFd(appContext.getString(R.string.fileAudioName));
        atc.loadFile(afdClack.getFileDescriptor(), afdClackFinal.getFileDescriptor());
        atc.initialize(30,300);
        atc.play();
        assertEquals(atc.getAudioTrack().getPlayState(), AudioTrack.PLAYSTATE_PLAYING);
    }

    @Test
    public void stop() throws Exception{
        Context appContext = InstrumentationRegistry.getTargetContext();
        AudioTrackController atc = new AudioTrackController();
        AssetFileDescriptor afdClack = appContext.getAssets().openFd(appContext.getString(R.string.fileAudioName));
        AssetFileDescriptor afdClackFinal = appContext.getAssets().openFd(appContext.getString(R.string.fileAudioName));
        atc.loadFile(afdClack.getFileDescriptor(), afdClackFinal.getFileDescriptor());
        atc.initialize(30,300);
        atc.play();
        assertEquals(atc.getAudioTrack().getState(), AudioTrack.PLAYSTATE_STOPPED);
        }
}
