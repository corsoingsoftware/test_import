package a2016.soft.ing.unipd.metronomepro;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioTrack;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import a2016.soft.ing.unipd.metronomepro.entities.ParcelableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.sound.management.AudioTrackController;
import a2016.soft.ing.unipd.metronomepro.sound.management.AudioTrackSongPlayer;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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

    /**
     * This test doesn't work, cause it use android library to produce a sound
     * @throws Exception
     */
    @Test
    public void songTest() throws Exception {

        TimeSlice t1, t2, t3;

        t1 = new TimeSlice();
        t2 = new TimeSlice();
        t3 = new TimeSlice();
        t1.setBpm(60);
        t1.setDurationInBeats(20);
        t2.setBpm(80);
        t2.setDurationInBeats(20);
        t3.setBpm(100);
        t3.setDurationInBeats(20);


        Song s = new ParcelableSong();
        s.add(t1);
        s.add(t2);
        s.add(t3);

        SongPlayer atsp = new AudioTrackSongPlayer();
        atsp.load(s);

        Song[] arrayS = new Song[1];
        arrayS[0] = s;

        ((AudioTrackSongPlayer)atsp).write(arrayS);
        //atsp.play();
    }

    @Test
    public void encodeDecodeTest() throws Exception {

        TimeSlice t1 = new TimeSlice();
        byte[] toreturn = t1.encode();
        TimeSlice t2 = new TimeSlice();
        t2.decode(toreturn);

        assertEquals(t1.getBpm(), t2.getBpm());
        assertEquals(t1.getDurationInBeats(), t2.getDurationInBeats());
        assertEquals(t1.getTimeFigureDenominator(), t2.getTimeFigureDenominator());
        assertEquals(t2.getTimeFigureNumerator(), t2.getTimeFigureNumerator());
    }
}
