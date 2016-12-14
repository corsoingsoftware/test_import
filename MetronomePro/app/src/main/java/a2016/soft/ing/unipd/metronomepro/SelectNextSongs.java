package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.adapters.SelectSongsAdapter;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableSong;
import a2016.soft.ing.unipd.metronomepro.entities.PlayableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

public class SelectNextSongs extends AppCompatActivity {

    private RecyclerView rVNextSongs;
    private RecyclerView.LayoutManager rVLayoutManager;
    private SelectSongsAdapter selectSongsAdapter;
    private final static int MAX_SELECTABLE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_next_songs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Playlist p = EntitiesBuilder.getPlaylist("pippo");
        Song s1 = EntitiesBuilder.getSong("S1");
        Song s2 = EntitiesBuilder.getSong("S2");
        Song s3 = EntitiesBuilder.getSong("S3");
        Song s4 = EntitiesBuilder.getSong("S4");
        Song s5 = EntitiesBuilder.getSong("S5");
        p.add(s1);
        p.add(s2);
        p.add(s3);
        p.add(s4);
        p.add(s5);

        rVNextSongs = (RecyclerView) findViewById(R.id.recycler_view_next_songs);
        rVNextSongs.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVNextSongs.setLayoutManager(rVLayoutManager);

        if(savedInstanceState.containsKey("Adapter")) {

            //Devo riscotruire il list adapter in modo che sia uguale a prima

            ArrayList<PlayableSong> savedArray = savedInstanceState.getParcelableArrayList("Adapter");
            int selectedSongs = savedInstanceState.getInt("Adapter");
            selectSongsAdapter = new SelectSongsAdapter(savedArray, selectedSongs,MAX_SELECTABLE);
            rVNextSongs.setAdapter(selectSongsAdapter);

        }
        else if (savedInstanceState.containsKey("Playlist")) {

            p = savedInstanceState.getParcelable("Playlist");
            selectSongsAdapter = new SelectSongsAdapter(this, p, 0, MAX_SELECTABLE);
            rVNextSongs.setAdapter(selectSongsAdapter);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Adapter", selectSongsAdapter.getArraySongs());
        outState.putInt("Adapter", selectSongsAdapter.getSelectedSongs());
    }
}
