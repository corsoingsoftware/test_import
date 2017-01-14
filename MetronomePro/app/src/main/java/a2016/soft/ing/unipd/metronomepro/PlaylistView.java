package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.adapters.SelectPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelablePlaylist;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;

public class PlaylistView extends AppCompatActivity {

    private RecyclerView rVPlaylistItem;
    private RecyclerView.LayoutManager rVLayoutManager;
    private SelectPlaylistAdapter playListAdapter;
    private ArrayList<ParcelablePlaylist> selectedPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        rVPlaylistItem = (RecyclerView)findViewById(R.id.recicle_view_playlist);
        rVPlaylistItem.setHasFixedSize(true);
        rVLayoutManager =  new LinearLayoutManager(this);
        rVPlaylistItem.setLayoutManager(rVLayoutManager);
        playListAdapter= new SelectPlaylistAdapter(this,createTestPlaylist());
        rVPlaylistItem.setAdapter(playListAdapter);

        if(savedInstanceState !=null && savedInstanceState.containsKey("playlist_for_select")){
            selectedPlaylist = savedInstanceState.getParcelableArrayList("playlist_for_select");

            /**
            savedSongs = savedInstanceState.getParcelableArrayList("song for select");
            selectedSongs = savedInstanceState.getParcelableArrayList(SONG_TO_ADD);
            selectSongForPlaylistAdapter = new SelectSongForPlaylistAdapter(this,savedSongs,selectedSongs);
            rVSelectSong.setAdapter(selectSongForPlaylistAdapter);
             */
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    protected void onSaveInstanceState(Bundle outState) {
        //prende le canzoni totali
        outState.putParcelableArrayList("playlist_for_select", (ArrayList<? extends Parcelable>) playListAdapter.getArrayPlaylist());

        super.onSaveInstanceState(outState);
    }
    //metodo di test per vedere se funziona
    private ArrayList<Playlist> createTestPlaylist(){
        Playlist p1 =  EntitiesBuilder.getPlaylist("prova di playlist 1");
        Playlist p2 =  EntitiesBuilder.getPlaylist("prova di playlist 2");
        Playlist p3 =  EntitiesBuilder.getPlaylist("prova di playlist 3");

        ArrayList<Playlist> arrayList = new ArrayList<>();
        //HARDCODED FOR TEST
        arrayList.add(0,p1);
        arrayList.add(1,p2);
        arrayList.add(2,p3);

        return arrayList;
    }



}
