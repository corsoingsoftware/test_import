package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.io.Serializable;

import a2016.soft.ing.unipd.metronomepro.adapters.ModifyPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.DragTouchHelperCallback;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelablePlaylist;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;

public class ModifyPlaylistActivity extends AppCompatActivity implements OnStartDragListener {

    private RecyclerView rVModifyPlaylist;
    private RecyclerView.LayoutManager rVLayoutManager;
    private ModifyPlaylistAdapter modifyPlaylistAdapter;
    private ItemTouchHelper itemTouchHelper;
    private Playlist playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rVModifyPlaylist = (RecyclerView) findViewById(R.id.recycler_view_songs_list);
        rVModifyPlaylist.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVModifyPlaylist.setLayoutManager(rVLayoutManager);
        modifyPlaylistAdapter = new ModifyPlaylistAdapter((ParcelablePlaylist) playlist, this, this);
        rVModifyPlaylist.setAdapter(modifyPlaylistAdapter);
        DragTouchHelperCallback myItemTouchHelper = new DragTouchHelperCallback(modifyPlaylistAdapter);
        itemTouchHelper = new ItemTouchHelper(myItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rVModifyPlaylist);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        playlist = EntitiesBuilder.getPlaylist("playlist di prova");
        playlist.add(EntitiesBuilder.getSong("Canzone 1"));
        playlist.add(EntitiesBuilder.getSong("Canzone 2"));
        playlist.add(EntitiesBuilder.getSong("Canzone 3"));
        playlist.add(EntitiesBuilder.getSong("Canzone 4"));
        playlist.add(EntitiesBuilder.getSong("Canzone 5"));
        playlist.add(EntitiesBuilder.getSong("Canzone 6"));

        savedInstanceState.putSerializable("Adapter", (Serializable)playlist);
        if(savedInstanceState.containsKey("Adapter")){
            Playlist savedPlaylist = savedInstanceState.getParcelable("Adapter");
            modifyPlaylistAdapter = new ModifyPlaylistAdapter((ParcelablePlaylist) playlist, this, this);
            rVModifyPlaylist.setAdapter(modifyPlaylistAdapter);
        }

        else if(savedInstanceState.containsKey("Playlist")){
            playlist = savedInstanceState.getParcelable("Playlist");
            modifyPlaylistAdapter = new ModifyPlaylistAdapter((ParcelablePlaylist) playlist, this, this);
            rVModifyPlaylist.setAdapter(modifyPlaylistAdapter);
        }


    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }



    //solo una prova


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("Adapter", (Serializable) modifyPlaylistAdapter.getPlaylistToModify());

    }
}
