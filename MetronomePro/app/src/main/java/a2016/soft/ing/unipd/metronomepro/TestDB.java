package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProvider;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProviderBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.MidiSong;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;

public class TestDB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_db);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DataProvider dp = DataProviderBuilder.getDefaultDataProvider(this);
        MidiSong ms= EntitiesBuilder.getMidiSong();
        ms.setName("prova");
        ms.setPath("dsjbdfffk");

        TimeSlicesSong tss=EntitiesBuilder.getTimeSlicesSong();
        tss.setName("prova ts");
        TimeSlice ts= new TimeSlice();
        ts.setBpm(60);
        ts.setDurationInBeats(120);
        tss.add(ts);
        ts= new TimeSlice();
        ts.setBpm(60);
        ts.setDurationInBeats(120);
        tss.add(ts);
        ts= new TimeSlice();
        ts.setBpm(60);
        ts.setDurationInBeats(120);
        tss.add(ts);

        dp.saveSong(ms);
        dp.saveSong(tss);

        List<Song> songs=dp.getSongs();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
