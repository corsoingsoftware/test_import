package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import a2016.soft.ing.unipd.metronomepro.adapters.SelectPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.SelectSongForPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

public class SelectSongForPlaylist extends AppCompatActivity {

    private RecyclerView rVSelectSong;
    private RecyclerView.LayoutManager rVLayoutManager;
    private SelectSongForPlaylistAdapter selectSongForPlaylistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        rVSelectSong = (RecyclerView)findViewById(R.id.recicle_song_for_playlist);
        rVSelectSong.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVSelectSong.setLayoutManager(rVLayoutManager);
        selectSongForPlaylistAdapter = new SelectSongForPlaylistAdapter(this,provaDiTest());
        rVSelectSong.setAdapter(selectSongForPlaylistAdapter);

        //potrebbe essere la strada per permettere il click, per il momento non è quella giusta!
        /**
        rVSelectSong.setOnClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"hai selezionato un pulsante",Toast.LENGTH_SHORT).show();
            }
        });
        */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_song_for_playlist);
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
    }

    protected void onSaveInstanceState(Bundle outState) {
        //prende le canzoni totali e quelle selezionate
        outState.putParcelableArrayList("song for select", (ArrayList<ParcelableSong>) selectSongForPlaylistAdapter.getArraySongs());
        outState.putParcelableArrayList("song for select", (ArrayList<ParcelableSong>)selectSongForPlaylistAdapter.getSelectedSongs());
        super.onSaveInstanceState(outState);
    }

    public ArrayList<ParcelableSong> provaDiTest(){
        ArrayList<ParcelableSong> array = new ArrayList<>();

        Song s1 = EntitiesBuilder.getSong("prova di canzone 1");
        Song s2 = EntitiesBuilder.getSong("prova di canzone 2");
        Song s3 = EntitiesBuilder.getSong("prova di canzone 3");

        array.add((ParcelableSong) s1);
        array.add((ParcelableSong) s2);
        array.add((ParcelableSong) s3);

        return array;

    }

}
