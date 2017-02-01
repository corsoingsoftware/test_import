package a2016.soft.ing.unipd.metronomepro;

import android.app.Activity;
import android.app.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import a2016.soft.ing.unipd.metronomepro.adapters.SelectPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.DragTouchHelperCallback;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProvider;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProviderBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
/**
 * Created by giuli on 27/12/2016.
 */
public class PlaylistView extends AppCompatActivity implements SelectPlaylistAdapter.OnPlaylistClickListener, OnStartDragListener {

    private RecyclerView rVPlaylistItem;
    private RecyclerView.LayoutManager rVLayoutManager;
    private SelectPlaylistAdapter playListAdapter;
    private ArrayList<Playlist> selectedPlaylist;
    private ItemTouchHelper itemTouchHelper;
    private DataProvider db;

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

        db = DataProviderBuilder.getDefaultDataProvider(this);
        List<String> playlistNames = db.getAllPlaylists();
        selectedPlaylist= new ArrayList<>();
        for (String s:playlistNames){
            Playlist p = db.getPlaylist(s);
            selectedPlaylist.add(p);
        }

        playListAdapter= new SelectPlaylistAdapter(this,selectedPlaylist,this,this);
        rVPlaylistItem.setAdapter(playListAdapter);
        DragTouchHelperCallback myItemTouchHelper = new DragTouchHelperCallback(playListAdapter);
        itemTouchHelper = new ItemTouchHelper(myItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rVPlaylistItem);

        if(savedInstanceState !=null && savedInstanceState.containsKey("playlist_for_select")){
            selectedPlaylist = savedInstanceState.getParcelableArrayList("playlist_for_select");
            playListAdapter = new SelectPlaylistAdapter(this,selectedPlaylist,this,this);
            rVPlaylistItem.setAdapter(playListAdapter);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
               customDialog();

            }
        });
    }
    protected void onSaveInstanceState(Bundle outState) {
        //prende le canzoni totali
        outState.putParcelableArrayList("playlist_for_select", (ArrayList<? extends Parcelable>) playListAdapter.getArrayPlaylist());

        super.onSaveInstanceState(outState);
    }

    public void customDialog(){
        final Dialog dialog = new Dialog(PlaylistView.this);
        dialog.setContentView(R.layout.dialog_submit);
        final EditText edit_name = (EditText) dialog.findViewById(R.id.edit_name_p);
        Button cancel = (Button) dialog.findViewById(R.id.but_cancel_p);
        Button submit = (Button) dialog.findViewById(R.id.but_submit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Playlist p = EntitiesBuilder.getPlaylist(edit_name.getText().toString());
                playListAdapter.addPlaylist(p);
                dialog.cancel();
            }
        });
        dialog.show();

    }

    @Override
    public void onPlaylistClick() {
        Activity activity = this;
        Intent intent = new Intent(activity,ModifyPlaylistActivity.class);
        Playlist p = playListAdapter.getPlaylistToEdit();
        intent.putExtra(ActivityExtraNames.PLAYLIST_SELECTED,playListAdapter.getPlaylistToEdit());
        Playlist playList = playListAdapter.getPlaylistToEdit();
        startActivity(intent);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
    }
}
