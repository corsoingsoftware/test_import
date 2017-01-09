package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import a2016.soft.ing.unipd.metronomepro.adapters.SelectPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProvider;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProviderBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelablePlaylist;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

public class PlaylistView extends AppCompatActivity {

    private RecyclerView rVPlaylistItem;
    private RecyclerView.LayoutManager rVLayoutManager;
    private SelectPlaylistAdapter playListAdapter;
    private ArrayList<ParcelableSong> playlistSongs;

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

        Bundle boundle = getIntent().getExtras();//sono tutte le canzoni presenti nella playlist che mi passano
        playlistSongs = boundle.getParcelableArrayList("songs_to_add");//nome della lista che lui chiama dalla sua classe


        playListAdapter= new SelectPlaylistAdapter(this,playlistSongs);
        rVPlaylistItem.setAdapter(playListAdapter);

        if(savedInstanceState != null && savedInstanceState.containsKey("adapter")){
            ArrayList<ParcelableSong> savedPlaylist = savedInstanceState.getParcelableArrayList("adapter");
            playListAdapter = new SelectPlaylistAdapter(this,savedPlaylist);
            rVPlaylistItem.setAdapter(playListAdapter);
        }



       /** else{
           qui dovrebbe "ricaricare" la lista e l'adapter direttamente dal database e non piu dal savedinstance state
        }*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
       /** fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                playListAdapter.remuvePlaylist(1);
            }
        });*/

    }
    //metodo di test per vedere se funziona
    private ArrayList<ParcelablePlaylist> createTestPlaylist(){
        Playlist p1 = EntitiesBuilder.getPlaylist("prova di playlist 1");
        Playlist p2 = EntitiesBuilder.getPlaylist("prova di playlist 2");
        Playlist p3 = EntitiesBuilder.getPlaylist("prova di playlist 3");

        ArrayList<ParcelablePlaylist> arrayList = new ArrayList<>();
        //HARDCODED FOR TEST
        arrayList.add(0, (ParcelablePlaylist) p1);
        arrayList.add(1, (ParcelablePlaylist) p2);
        arrayList.add(2, (ParcelablePlaylist) p3);

        return arrayList;
    }
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("adapter",playListAdapter.getArrayPlaylist());
        super.onSaveInstanceState(outState);
    }
}
