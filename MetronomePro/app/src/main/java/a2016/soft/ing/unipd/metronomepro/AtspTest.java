package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import a2016.soft.ing.unipd.metronomepro.entities.ParcelableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.sound.management.AudioTrackSongPlayer;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayer;

/**
 * This class is for testing android functions not testable by unit testing
 */
public class AtspTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atsp_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TimeSlice t1, t2, t3;

        Song s = new ParcelableSong();
        t1 = new TimeSlice();
        t1.setBpm(180);
        t1.setDurationInBeats(10);
        s.add(t1);
        t2 = new TimeSlice();
        t2.setBpm(300);
        t2.setDurationInBeats(10);
        s.add(t2);
        t3 = new TimeSlice();
        t3.setBpm(30);
        t3.setDurationInBeats(10);
        s.add(t3);



        SongPlayer atsp = new AudioTrackSongPlayer();
        atsp.load(s);

        Song[] arrayS = new Song[1];
        arrayS[0] = s;


        ((AudioTrackSongPlayer)atsp).write(arrayS);

        atsp.play();
    }
}
