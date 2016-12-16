package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import a2016.soft.ing.unipd.metronomepro.adapters.SelectPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelablePlaylist;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

public class PlaylistView extends AppCompatActivity {

    private RecyclerView rVPlaylistItem;
    private RecyclerView.LayoutManager rVLayoutManager;
    private SelectPlaylistAdapter playListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //per test
        Playlist p1 = EntitiesBuilder.getPlaylist("prova di playlist 1");
        Playlist p2 = EntitiesBuilder.getPlaylist("prova di playlist 2");
        Playlist p3 = EntitiesBuilder.getPlaylist("prova di playlist 3");

        rVPlaylistItem = (RecyclerView)findViewById(R.id.recicle_view_playlist);
        rVPlaylistItem.setHasFixedSize(true);
        rVLayoutManager =  new LinearLayoutManager(this);
        rVPlaylistItem.setLayoutManager(rVLayoutManager);
        //per test
        playListAdapter= new SelectPlaylistAdapter(this,p1);
        playListAdapter.addPlaylist(p2);
        playListAdapter.addPlaylist(p3);
        //speriamo bene
        rVPlaylistItem.setAdapter(playListAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private Playlist createTestPlaylist(){


        Playlist p = EntitiesBuilder.getPlaylist("prova di playlist");


        return p;


    }

}
