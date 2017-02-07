package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableTimeSlicesSong;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;
import a2016.soft.ing.unipd.metronomepro.sound.management.AudioTrackSongPlayer;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayer;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayerServiceCaller;

/**
 * This class is for testing android functions not testable by unit testing.
 * It creates a new TimeSlicesSong and tests if AudioTrackSongPlayer can reproduce it.
 */


public class AtspTest extends AppCompatActivity implements SongPlayer.SongPlayerCallback {

    private int durationForTest = 30;
    private int bpmForTest = 100;
    private AudioTrackSongPlayer audioTrackTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atsp_test);
        Toolbar toolbarActivityTest = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarActivityTest);

        /**
         * Initialize audioTrackTest and create a TimeSlicesSong object for testing.
         */

        audioTrackTest = new AudioTrackSongPlayer(this);
        TimeSlicesSong songTest = EntitiesBuilder.getTimeSlicesSong();
        TimeSlice timeSliceTest = new TimeSlice();
        timeSliceTest.setDurationInBeats(durationForTest);
        timeSliceTest.setBpm(bpmForTest);
        songTest.add(timeSliceTest);
        Song[] arraySongTest = new Song[1];
        arraySongTest[0] = songTest;

        /**
         * I logically load the song, write it in the audioTrackTest's buffer and I play it.
         */

        audioTrackTest.load(songTest);
        audioTrackTest.write(arraySongTest);
        audioTrackTest.play();
    }

    @Override
    public void playEnded(SongPlayer origin) {

        /**
         * Useless for this test.
         */
    }
}
