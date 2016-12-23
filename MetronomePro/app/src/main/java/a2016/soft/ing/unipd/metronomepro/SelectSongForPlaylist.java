package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
        /**
         *  rVPlaylistItem = (RecyclerView)findViewById(R.id.recicle_view_playlist);
         rVPlaylistItem.setHasFixedSize(true);
         rVLayoutManager =  new LinearLayoutManager(this);
         rVPlaylistItem.setLayoutManager(rVLayoutManager);
         playListAdapter= new SelectPlaylistAdapter(this,createTestPlaylist());
         rVPlaylistItem.setAdapter(playListAdapter);
         */
        rVSelectSong = (RecyclerView)findViewById(R.id.recicle_song_for_playlist);
        rVSelectSong.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVSelectSong.setLayoutManager(rVLayoutManager);
        selectSongForPlaylistAdapter = new SelectSongForPlaylistAdapter(this,provaDiTest());//manca la lista da inserire
        rVSelectSong.setAdapter(selectSongForPlaylistAdapter);



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
